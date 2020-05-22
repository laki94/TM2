package pl.polsl.laboratorioweobecnosci.activities.admin

import android.content.Intent
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
import pl.polsl.laboratorioweobecnosci.database.models.lists.LaboratoryList

class GenerateCSVActivity : AppCompatActivity() {

    private lateinit var adapter: GenerateCSVAdapter
    private lateinit var laboratories: LaboratoryList


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_generate_csv)

        Thread {
            val db = DatabaseHandler(this)
            laboratories = db.getAllLaboratories()
            runOnUiThread {
                adapter = GenerateCSVAdapter(this, laboratories)

                adapter.let {
                    it.onGenerateClick = {lab ->
                        val generator = CsvGenerator(this, ::doOnGenerated)
                        generator.generate(lab.id)
                    }
                }
                val rvList = findViewById<RecyclerView>(R.id.rvLabListCSV)
                rvList.layoutManager = LinearLayoutManager(this)
                rvList.adapter = adapter
            }
        }.start()
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
