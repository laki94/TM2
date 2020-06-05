package pl.polsl.laboratorioweobecnosci.activities.admin

import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import pl.polsl.laboratorioweobecnosci.R
import pl.polsl.laboratorioweobecnosci.activities.BaseActivity
import pl.polsl.laboratorioweobecnosci.activities.adapters.RateListAdapter
import pl.polsl.laboratorioweobecnosci.csv.CsvGenerator
import pl.polsl.laboratorioweobecnosci.database.DatabaseHandler
import pl.polsl.laboratorioweobecnosci.database.models.LaboratoryWorkstationGradeModel
import pl.polsl.laboratorioweobecnosci.database.models.lists.LaboratoryTaskList
import pl.polsl.laboratorioweobecnosci.database.models.lists.ListOfWorkstationsWithStudents
import pl.polsl.laboratorioweobecnosci.preferences.PermissionsManager

class RateActivity : BaseActivity() {

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

    fun onSaveRatesClick(view: View) {
        generateCsvFile()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            PermissionsManager.WRITE_EXTERNAL_STORAGE_REQ_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    generateCsvFile()
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private fun generateCsvFile() {
        if (PermissionsManager.instance.haveWriteExternalPermission(this)) {
            val csv = CsvGenerator(this, ::onGenerated)
            csv.generate(labId)
        } else {
            PermissionsManager.instance.askForWriteExternalPermission(this)
        }
    }

    private fun onGenerated(success: Boolean, message: String) {
        runOnUiThread {
            if (success) {
                Toast.makeText(this, getString(R.string.SavedFile, message), Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, getString(R.string.CouldNotSaveFile, message), Toast.LENGTH_LONG).show()
            }
        }
    }
}
