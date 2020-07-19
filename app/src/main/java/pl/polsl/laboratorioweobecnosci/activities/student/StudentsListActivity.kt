package pl.polsl.laboratorioweobecnosci.activities.student

import android.graphics.*
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import pl.polsl.laboratorioweobecnosci.R
import pl.polsl.laboratorioweobecnosci.activities.adapters.ListOfStudentsAtWorkstationAdapter
import pl.polsl.laboratorioweobecnosci.database.DatabaseHandler
import pl.polsl.laboratorioweobecnosci.database.models.*
import pl.polsl.laboratorioweobecnosci.security.AuthorizationManager

/**
 * Aktywność ze studentami dla laboratorium
 * @property listOfStudentsAtWorkstationAdapter adapter dla RecyclerView aktywności
 * @property studentsAtLaboratory lista studentów na laboratorium
 * @property mainLaboratory laboratorium na którym są studenci
 * @property mainRecyclerView RecyclerView na aktywności
 * @property backButtonNeedAuth określa czy trzeba autoryzować się po próbie powrotu do ekranu głównego
 */
class StudentsListActivity : AppCompatActivity() {

    private lateinit var listOfStudentsAtWorkstationAdapter: ListOfStudentsAtWorkstationAdapter
    private lateinit var studentsAtLaboratory: ListOfStudentsAtWorkstation
    private lateinit var mainLaboratory: Laboratory
    private lateinit var mainRecyclerView: RecyclerView
    private var backButtonNeedAuth = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_students_list)

        val tvLaboratory = findViewById<TextView>(R.id.tvLaboratory)

        val extras = intent.extras
        Thread {
            val db = DatabaseHandler(this)
            studentsAtLaboratory = db.getStudentsAssignedToWorkstationsAtLaboratory(extras!!.getInt("LABID", 0))
            mainLaboratory = db.laboratoryDao().getLaboratory(extras.getInt("LABID", 0))
            runOnUiThread {
                listOfStudentsAtWorkstationAdapter = ListOfStudentsAtWorkstationAdapter(this, studentsAtLaboratory)

                listOfStudentsAtWorkstationAdapter.onStudentEdit = {
                    editStudent(it)
                }

                listOfStudentsAtWorkstationAdapter.onStudentRemove = { remModel ->
                    val snackbar = Snackbar.make(findViewById(R.id.clStudents),
                        R.string.StudentRemoved, Snackbar.LENGTH_INDEFINITE)
                    snackbar.setAction("UNDO") {
                        listOfStudentsAtWorkstationAdapter.addNewStudent(remModel)
                    }
                    snackbar.addCallback(object : Snackbar.Callback() {
                        override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                            if (event != DISMISS_EVENT_ACTION) {
                                Thread {
                                    db.studentDao().delete(remModel.student)
                                    if (db.getStudentsAtWorkstation(mainLaboratory.id, remModel.workstation.id).isEmpty()) {
                                        val remGrade = db.laboratoryWorkstationGradeDao().getGradeForWorkstationAtLaboratory(mainLaboratory.id, remModel.workstation.id)
                                        val remTasks = db.workstationLaboratoryTaskDao().getTasksDoneForWorkstationAtLaboratory(remModel.workstation.id, mainLaboratory.id)
                                        if (remGrade != null) {
                                            db.laboratoryWorkstationGradeDao().delete(remGrade)
                                        }
                                        remTasks.forEach {
                                            db.workstationLaboratoryTaskDao().delete(it)
                                        }
                                    }

                                }.start()
                            }
                            super.onDismissed(transientBottomBar, event)
                        }
                    })
                    snackbar.setActionTextColor(Color.YELLOW)
                    snackbar.show()
                }

                mainRecyclerView = findViewById(R.id.rvStudentsWorkstations)
                mainRecyclerView.layoutManager = LinearLayoutManager(this)
                mainRecyclerView.adapter = listOfStudentsAtWorkstationAdapter

                tvLaboratory.text = mainLaboratory.laboratoryName
                if (extras.getBoolean("ADMINMODE", false)) {
                    backButtonNeedAuth = false
                    listOfStudentsAtWorkstationAdapter.enableSwipe()
                    val fab = findViewById<FloatingActionButton>(R.id.fabAddStudent)
                    fab.hide()
                }
            }
        }.start()
    }

    override fun onBackPressed() {
        if (backButtonNeedAuth) {
            AuthorizationManager.getInstance().doAuthorize(this) { doOnBackPressed() }
        } else {
            doOnBackPressed()
        }
    }

    private fun doOnBackPressed() {
        super.onBackPressed()
    }

    /**
     * Funkcja wywołana po wciśnięciu przycisku dodawania studenta. Wyświetla dialog z dodaniem studenta
     */
    fun onAddStudentClick(view: View) {
        val dialog = StudentDialog(this)
        dialog.onSaveClick = {
            Thread {
                val db = DatabaseHandler(this)
                var workstationId = db.workstationDao().getWorkstationId(it.workstation.number)
                if (workstationId == 0)
                    workstationId = db.workstationDao().insert(it.workstation).toInt()
                it.student.workstationId = workstationId
                db.studentDao().insert(it.student)
                runOnUiThread {
                    listOfStudentsAtWorkstationAdapter.addNewStudent(it)
                }
            }.start()
        }
        dialog.addStudent(layoutInflater, mainLaboratory.id)
    }

    /**
     * Wyświetla dialog z edycją studenta
     * @param studentWorkstation edytowany student
     */
    private fun editStudent(studentWorkstation: StudentWorkstationModel) {
        val dialog = StudentDialog(this)
        dialog.onSaveClick = {
            Thread {
                val db = DatabaseHandler(this)
                var workstationId = db.workstationDao().getWorkstationId(it.workstation.number)
                if (workstationId == 0)
                    workstationId = db.workstationDao().insert(it.workstation).toInt()
                it.student.workstationId = workstationId

                db.studentDao().update(it.student)

                if (!it.workstation.compare(studentWorkstation.workstation)) {
                    val orgTasks = db.workstationLaboratoryTaskDao().getTasksDoneForWorkstationAtLaboratory(studentWorkstation.workstation.id, mainLaboratory.id)

                    if (db.getStudentsAtWorkstation(mainLaboratory.id, studentWorkstation.workstation.id).isEmpty()) {
                        orgTasks.forEach {task ->
                            db.workstationLaboratoryTaskDao().delete(task)
                        }
                        val oldGrade = db.laboratoryWorkstationGradeDao().getGradeForWorkstationAtLaboratory(mainLaboratory.id, studentWorkstation.workstation.id)
                        if (oldGrade != null) {
                            db.laboratoryWorkstationGradeDao().delete(oldGrade)
                        }
                    }
                }

                runOnUiThread {
                    listOfStudentsAtWorkstationAdapter.editStudent(it)
                }
            }.start()
        }
        dialog.editStudent(studentWorkstation, layoutInflater)
    }
}
