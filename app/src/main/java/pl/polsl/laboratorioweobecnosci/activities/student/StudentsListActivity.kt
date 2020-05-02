package pl.polsl.laboratorioweobecnosci.activities.student

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import pl.polsl.laboratorioweobecnosci.R

class StudentsListActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_students_list)
    }

    fun onAddStudentClick(view: View) {
        val dialog = StudentDialog(this)
        dialog.addStudent(layoutInflater)
    }
}
