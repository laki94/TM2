package pl.polsl.laboratorioweobecnosci.activities.admin

import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import pl.polsl.laboratorioweobecnosci.R
import pl.polsl.laboratorioweobecnosci.activities.adapters.GenerateCSVAdapter
import pl.polsl.laboratorioweobecnosci.csv.CsvGenerator
import pl.polsl.laboratorioweobecnosci.database.DatabaseHandler
import pl.polsl.laboratorioweobecnosci.database.models.Laboratory
import pl.polsl.laboratorioweobecnosci.database.models.lists.LaboratoryList
import pl.polsl.laboratorioweobecnosci.preferences.PermissionsManager

/**
 * Aktywność wyświetlająca laboratoria dla których można wygenerować pliki CSV
 * @property adapter adapter dla RecyclerView aktywności
 * @property laboratories lista stworzonych laboratoriów
 * @property tempLaboratory laboratorium dla którego zostanie stworzony plik CSV
 */
class GenerateCSVActivity : AppCompatActivity() {

    private lateinit var adapter: GenerateCSVAdapter
    private lateinit var laboratories: LaboratoryList
    private var tempLaboratory: Laboratory? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_generate_csv)

        doInitialize()
    }

    /**
     * Pobranie stworzonych laboratoriów posortowanych po dacie, aktywność jest zamykana jeśli nie
     * ma żadnego.
     */
    private fun doInitialize() {
        Thread {
            val db = DatabaseHandler(this)
            laboratories = db.getLaboratoriesSortedByStartDate()
            runOnUiThread {
                if (laboratories.isEmpty()) {
                    Toast.makeText(this, R.string.NoLaboratoriesToShow, Toast.LENGTH_LONG).show()
                    finish()
                } else {
                    adapter = GenerateCSVAdapter(this, laboratories)

                    adapter.let {
                        it.onGenerateClick = { lab ->
                            tempLaboratory = lab
                            generateCSVFile()
                        }
                    }
                    val rvList = findViewById<RecyclerView>(R.id.rvLabListCSV)
                    rvList.layoutManager = LinearLayoutManager(this)
                    rvList.adapter = adapter
                }
            }
        }.start()
    }

    /**
     * Sprawdzenie uprawnień do zapisu i wygenerowanie pliku CSV
     */
    private fun generateCSVFile() {
        with (tempLaboratory) {
            if (this != null) {
                if (PermissionsManager.getInstance().haveWriteExternalPermission(this@GenerateCSVActivity)) {
                    val generator = CsvGenerator(this@GenerateCSVActivity, ::doOnGenerated)
                    generator.generate(this.id)
                } else {
                    PermissionsManager.getInstance().askForWriteExternalPermission(this@GenerateCSVActivity)
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

    /**
     * Funkcja, która zostaje wywołana po wygenerowaniu pliku CSV informująca użytkownika o statusie
     * pliku.
     */
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
