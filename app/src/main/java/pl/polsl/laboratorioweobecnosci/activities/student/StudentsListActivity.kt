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
        val builder = AlertDialog.Builder(this)
        val inflater = layoutInflater
        builder.setTitle(R.string.AddStudent)
        val dialogLayout = inflater.inflate(R.layout.activity_student, null)
        builder.setView(dialogLayout)
        builder.setPositiveButton(R.string.Save) { _, _ -> }
        builder.show()
    }
}
