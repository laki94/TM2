package pl.polsl.laboratorioweobecnosci.activities.admin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import pl.polsl.laboratorioweobecnosci.R
import pl.polsl.laboratorioweobecnosci.database.DatabaseHandler
import pl.polsl.laboratorioweobecnosci.database.models.lists.LaboratoryList

class AdminActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_panel)
    }

    fun onExercisesClick(view: View) {
        val intent = Intent(this, LaboratoriesActivity::class.java)
        startActivity(intent)
    }

    fun onRateClick(view: View) {
        Thread {
            val db = DatabaseHandler(this)
            val laboratories = db.getAllLaboratories()
            runOnUiThread {
                val dialog = LaboratoriesDialog(this)
                dialog.showLaboratoriesForRating(layoutInflater, laboratories)
            }
        }.start()
    }

    fun onGenerateCSVClick(view: View) {
        /* TODO */
    }
}
