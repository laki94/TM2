package pl.polsl.laboratorioweobecnosci.activities.admin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import pl.polsl.laboratorioweobecnosci.R
import pl.polsl.laboratorioweobecnosci.activities.adapters.RateListAdapter
import pl.polsl.laboratorioweobecnosci.database.DatabaseHandler
import pl.polsl.laboratorioweobecnosci.database.models.LaboratoryWorkstationGradeModel
import pl.polsl.laboratorioweobecnosci.database.models.lists.LaboratoryTaskList
import pl.polsl.laboratorioweobecnosci.database.models.lists.ListOfWorkstationsWithStudents

class RateActivity : AppCompatActivity() {

    private lateinit var adapter: RateListAdapter
    private var labId = 0
    private var workstationsWithStudents = ListOfWorkstationsWithStudents()
    private var tasks = LaboratoryTaskList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rate)

        val extras = intent.extras
        if (extras != null) {
            labId = extras.getInt("LABID", 0)
        }

        adapter = RateListAdapter(this, workstationsWithStudents)

        adapter.let {
            it.onWorkstationClick = { studentsAtWorkstation ->
                Thread {
                    val db = DatabaseHandler(this)
                    val tasksDone = db.getTasksDoneByWorkstationAtLaboratory(
                        studentsAtWorkstation.students[0].laboratoryId,
                        studentsAtWorkstation.workstation.id)
                    var actGrade = db.laboratoryGradeDao().getGradeForWorkstationAtLaboratory(
                        studentsAtWorkstation.students[0].laboratoryId,
                        studentsAtWorkstation.workstation.id
                    )
                    if (actGrade == null) {
                        actGrade = LaboratoryWorkstationGradeModel(
                            studentsAtWorkstation.students[0].laboratoryId,
                            studentsAtWorkstation.workstation.id)
                    }
                    runOnUiThread {
                        val dialog = RateDialog(this)
                        dialog.rate(layoutInflater, studentsAtWorkstation, tasks, tasksDone, actGrade)
                    }
                }.start()
            }
        }

        val list = findViewById<RecyclerView>(R.id.rvWorkstations)
        list.layoutManager = LinearLayoutManager(this)
        list.adapter = adapter

        Thread {
            val db = DatabaseHandler(this)
            tasks = db.getTasksForLaboratory(labId)
            val studentsAtLaboratory = db.getStudentsAtLaboratory(labId)
            studentsAtLaboratory.forEach {
                workstationsWithStudents.addStudentToWorkstation(it, db.workstationDao()
                    .getWorkstation(it.workstationId))
            }
            runOnUiThread {
                adapter.notifyDataSetChanged()
            }
        }.start()
    }

    private fun showRateSingleWorkstationDialog() {
        val dialog = RateWorkstationDialog(this)
        //Tu jest zły dialog
//        dialog.rate(layoutInflater, StudentWorkstationLaboratory(Workstation(1), StudentsList(this.studentDao().getStudentsOnWorkstation(laboratoryId, it)))) //TODO
    }

    fun onSaveRatesClick(view: View) {
        /* TODO */
    }
}
