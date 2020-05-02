package pl.polsl.laboratorioweobecnosci.activities.student

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import pl.polsl.laboratorioweobecnosci.R

class StudentDialog(context: Context) : AlertDialog.Builder(context) {

    fun addStudent(layoutInflater: LayoutInflater) {
        val dialogLayout = layoutInflater.inflate(R.layout.activity_student, null)
        setTitle(R.string.AddStudent)
        setView(dialogLayout)
        setPositiveButton(R.string.Save) { _, _ -> }

        super.show()
    }
}
