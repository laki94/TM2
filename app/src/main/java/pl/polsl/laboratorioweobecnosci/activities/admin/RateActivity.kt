package pl.polsl.laboratorioweobecnosci.activities.admin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import pl.polsl.laboratorioweobecnosci.R
import pl.polsl.laboratorioweobecnosci.RateDialog
import pl.polsl.laboratorioweobecnosci.database.DatabaseHandler
import pl.polsl.laboratorioweobecnosci.database.models.ListOfStudentsAtWorkstation
import pl.polsl.laboratorioweobecnosci.database.models.lists.LaboratoryTaskList
import pl.polsl.laboratorioweobecnosci.database.models.lists.ListOfWorkstationsWithStudents
import pl.polsl.laboratorioweobecnosci.database.models.lists.StudentList
import pl.polsl.laboratorioweobecnosci.database.models.lists.StudentWorkstationLaboratoryList

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
                val tasksDone = LaboratoryTaskList()
                Thread {
                    val db = DatabaseHandler(this)
                    val tmpTasksDone = db.workstationLaboratoryTaskDao()
                        .getTasksDoneForWorkstationAtLaboratory(studentsAtWorkstation.workstation.id,
                        studentsAtWorkstation.students[0].laboratoryId)
                    tmpTasksDone.iterator().forEach {workstationTask ->
                        val task = db.laboratoryTaskDao().getTaskWithId(workstationTask.laboratoryTaskId)
                        tasksDone.add(task)
                    }
                    runOnUiThread {
                        val dialog = RateDialog(this)
                        dialog.rate(layoutInflater, studentsAtWorkstation, tasks, tasksDone)
                    }
                }.start()
            }
        }

        val list = findViewById<RecyclerView>(R.id.rvWorkstations)
        list.layoutManager = LinearLayoutManager(this)
        list.adapter = adapter

        Thread {
            val db = DatabaseHandler(this)
            db.laboratoryTaskDao().getTasksForClass(labId).forEach {
                tasks.add(it)
            }

            db.laboratoryDao().getLaboratoryStudents(labId).iterator().forEachRemaining {
                workstationsWithStudents.addStudentToWorkstation(it, db.workstationDao().getWorkstation(it.workstationId))
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
