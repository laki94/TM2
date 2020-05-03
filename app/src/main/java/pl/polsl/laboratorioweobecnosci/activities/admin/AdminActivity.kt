package pl.polsl.laboratorioweobecnosci.activities.admin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import pl.polsl.laboratorioweobecnosci.R

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
        val intent = Intent(this, RateActivity::class.java)
        startActivity(intent)
    }

    fun onGenerateCSVClick(view: View) {
        /* TODO */
    }
}
