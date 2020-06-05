package pl.polsl.laboratorioweobecnosci.activities.admin

import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import pl.polsl.laboratorioweobecnosci.R
import pl.polsl.laboratorioweobecnosci.activities.adapters.GenerateCSVAdapter
import pl.polsl.laboratorioweobecnosci.activities.adapters.LaboratoryAdapter
import pl.polsl.laboratorioweobecnosci.activities.student.StudentsListActivity
import pl.polsl.laboratorioweobecnosci.csv.CsvGenerator
import pl.polsl.laboratorioweobecnosci.database.DatabaseHandler
import pl.polsl.laboratorioweobecnosci.database.models.Laboratory
import pl.polsl.laboratorioweobecnosci.database.models.lists.LaboratoryList
import pl.polsl.laboratorioweobecnosci.preferences.PermissionsManager

class GenerateCSVActivity : AppCompatActivity() {

    private lateinit var adapter: GenerateCSVAdapter
    private lateinit var laboratories: LaboratoryList

    private var tempLaboratory: Laboratory? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_generate_csv)

        Thread {
            val db = DatabaseHandler(this)
            laboratories = db.getLaboratoriesSortedByStartDate()
            runOnUiThread {
                adapter = GenerateCSVAdapter(this, laboratories)

                adapter.let {
                    it.onGenerateClick = {lab ->
                        tempLaboratory = lab
                        generateCSVFile()
                    }
                }
                val rvList = findViewById<RecyclerView>(R.id.rvLabListCSV)
                rvList.layoutManager = LinearLayoutManager(this)
                rvList.adapter = adapter
            }
        }.start()
    }

    private fun generateCSVFile() {
        with (tempLaboratory) {
            if (this != null) {
                if (PermissionsManager.instance.haveWriteExternalPermission(this@GenerateCSVActivity)) {
                    val generator = CsvGenerator(this@GenerateCSVActivity, ::doOnGenerated)
                    generator.generate(this.id)
                } else {
                    PermissionsManager.instance.askForWriteExternalPermission(this@GenerateCSVActivity)
                }
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            PermissionsManager.WRITE_EXTERNAL_STORAGE_REQ_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    generateCSVFile()
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private fun doOnGenerated(success: Boolean, message: String) {
        runOnUiThread {
            if (success) {
                Toast.makeText(this, getString(R.string.SavedFile, message), Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, getString(R.string.CouldNotSaveFile, message), Toast.LENGTH_LONG).show()
            }
        }
    }
}
