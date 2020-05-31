package pl.polsl.laboratorioweobecnosci.activities.student

import android.graphics.*
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.biometric.BiometricConstants
import androidx.biometric.BiometricPrompt
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import pl.polsl.laboratorioweobecnosci.R
import pl.polsl.laboratorioweobecnosci.activities.adapters.ListOfStudentsAtWorkstationAdapter
import pl.polsl.laboratorioweobecnosci.database.DatabaseHandler
import pl.polsl.laboratorioweobecnosci.database.models.*
import pl.polsl.laboratorioweobecnosci.preferences.AuthorizationMode
import pl.polsl.laboratorioweobecnosci.preferences.PreferencesManager
import pl.polsl.laboratorioweobecnosci.security.AuthorizationManager
import pl.polsl.laboratorioweobecnosci.security.FingerprintAuth

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
                                        val remGrade = db.laboratoryGradeDao().getGradeForWorkstationAtLaboratory(mainLaboratory.id, remModel.workstation.id)
                                        val remTasks = db.workstationLaboratoryTaskDao().getTasksDoneForWorkstationAtLaboratory(remModel.workstation.id, mainLaboratory.id)
                                        if (remGrade != null) {
                                            db.laboratoryGradeDao().delete(remGrade)
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
            AuthorizationManager.instance.doAuthorize({ doOnBackPressed() }, this)
        } else {
            doOnBackPressed()
        }
    }

    private fun doOnBackPressed() {
        super.onBackPressed()
    }

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
                        val oldGrade = db.laboratoryGradeDao().getGradeForWorkstationAtLaboratory(mainLaboratory.id, studentWorkstation.workstation.id)
                        if (oldGrade != null) {
                            db.laboratoryGradeDao().delete(oldGrade)
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
