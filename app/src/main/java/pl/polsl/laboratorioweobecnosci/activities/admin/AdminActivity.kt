package pl.polsl.laboratorioweobecnosci.activities.admin

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import pl.polsl.laboratorioweobecnosci.R
import pl.polsl.laboratorioweobecnosci.database.models.Workstation

class AdminActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_panel)
    }

    fun onExercisesClick(view: View) {
        val intent = Intent(this, ExercisesActivity::class.java)
        startActivity(intent)
    }

    fun onRateClick(view: View) {
        val intent = Intent(this, RateActivity::class.java)
        startActivity(intent)
    }

    fun onGenerateCSVClick(view: View) {
        /* TODO */
    }
}
