package pl.polsl.laboratorioweobecnosci.activities.admin

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import pl.polsl.laboratorioweobecnosci.R
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class ExercisesActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exercises)
    }

    fun onAddExerciseClick(view: View) {
        val dialog = ExerciseActivity(this)
        dialog.showAddDialog(layoutInflater)
    }
}
