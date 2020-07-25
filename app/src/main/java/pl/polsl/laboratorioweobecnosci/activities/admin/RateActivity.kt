package pl.polsl.laboratorioweobecnosci.activities.admin

import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import pl.polsl.laboratorioweobecnosci.R
import pl.polsl.laboratorioweobecnosci.activities.adapters.RateListAdapter
import pl.polsl.laboratorioweobecnosci.csv.CsvGenerator
import pl.polsl.laboratorioweobecnosci.database.DatabaseHandler
import pl.polsl.laboratorioweobecnosci.database.models.LaboratoryWorkstationGradeModel
import pl.polsl.laboratorioweobecnosci.database.models.lists.LaboratoryTaskList
import pl.polsl.laboratorioweobecnosci.database.models.WorkstationWithLabDetails
import pl.polsl.laboratorioweobecnosci.database.models.lists.WorkstationWithLabDetailsList
import pl.polsl.laboratorioweobecnosci.preferences.PermissionsManager

/**
 * Aktywność z ocenianiem stanowisk na laboratorium
 * @property adapter adapter do RecyclerView aktywności
 * @property labId Id ocenianego laboratorium
 * @property workstationDetails lista stanowisk ze studentami, zadaniami do wykonania, wykonanymi i przypisanej ocenie
 * @property tasks zadania do wykonania na laboratorium
 */
class RateActivity : AppCompatActivity() {

    private lateinit var adapter: RateListAdapter
    private var labId = 0
    private var workstationDetails = WorkstationWithLabDetailsList()
    private var tasks = LaboratoryTaskList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rate)

        val extras = intent.extras
        if (extras != null) {
            labId = extras.getInt("LABID", 0)
        }

        adapter = RateListAdapter(this, workstationDetails)

        adapter.let {
            it.onWorkstationClick = { studentsAtWorkstation ->
                val dialog = RateDialog(this)
                dialog.setOnDismissListener {
                    adapter.notifyDataSetChanged()
                }
                dialog.rate(layoutInflater, studentsAtWorkstation)
            }
        }

        val list = findViewById<RecyclerView>(R.id.rvWorkstations)
        list.layoutManager = LinearLayoutManager(this)
        list.adapter = adapter

        Thread {
            val db = DatabaseHandler(this)
            workstationDetails.addAll(db.getAllWorkstationsDetails(labId))
            runOnUiThread {
                if (workstationDetails.isEmpty()) {
                    Toast.makeText(this, R.string.NoStudentsToRate, Toast.LENGTH_LONG).show()
                    finish()
                } else {
                    adapter.notifyDataSetChanged()
                }
            }
        }.start()
    }

    /**
     * Funkcja wywołana po wciśnięciu przycisku zapisania ocen generująca plik CSV
     */
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

    /**
     * Generowanie pliku CSV
     */
    private fun generateCsvFile() {
        if (PermissionsManager.getInstance().haveWriteExternalPermission(this)) {
            val csv = CsvGenerator(this, ::onGenerated)
            csv.generate(labId)
        } else {
            PermissionsManager.getInstance().askForWriteExternalPermission(this)
        }
    }

    /**
     * Callback wywołany po wygenerowaniu pliku CSV
     */
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
