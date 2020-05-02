package pl.polsl.laboratorioweobecnosci.activities.admin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import pl.polsl.laboratorioweobecnosci.R

class ExercisesActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exercises)
    }

    fun onAddExerciseClick(view: View) {
        val dialog = ExerciseDialog(this)
        dialog.showAddDialog(layoutInflater)
    }
}
