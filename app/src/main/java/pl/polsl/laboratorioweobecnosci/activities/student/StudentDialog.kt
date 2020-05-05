package pl.polsl.laboratorioweobecnosci.activities.student

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import pl.polsl.laboratorioweobecnosci.R
import pl.polsl.laboratorioweobecnosci.database.DatabaseHandler
import pl.polsl.laboratorioweobecnosci.database.models.LaboratoryTaskModel
import pl.polsl.laboratorioweobecnosci.database.models.Student
import pl.polsl.laboratorioweobecnosci.database.models.Workstation

class StudentDialog(context: Context) : AlertDialog.Builder(context) {

    var onSaveClick: ((Student) -> Unit)? = null

    private var mainStudent = Student("", "", 0, 0)
    private lateinit var dialogLayout: View
    private lateinit var etWorkstationNr: EditText

    fun addStudent(layoutInflater: LayoutInflater, laboratoryId: Int) {
        dialogLayout = layoutInflater.inflate(R.layout.activity_student, null)
        etWorkstationNr = dialogLayout.findViewById(R.id.etWorkstation)
        mainStudent.laboratoryId = laboratoryId

        setTitle(R.string.AddStudent)
        setView(dialogLayout)
        setPositiveButton(R.string.Save) { _, _ -> }

        val dialog = create()
        dialog.show()

        val btn = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
        btn.setOnClickListener {
            fillStudentInfo()
            if (mainStudent.firstName.isNotEmpty() && mainStudent.lastName.isNotEmpty() && etWorkstationNr.text.isNotEmpty()) {
                Thread {
                    val db = DatabaseHandler(context)
                    var workstationId = db.workstationDao().getWorkstationId(etWorkstationNr.text.toString().toInt())
                    if (workstationId == 0)
                        workstationId = db.workstationDao().insert(Workstation(etWorkstationNr.text.toString().toInt())).toInt()
                    mainStudent.workstationId = workstationId
                    db.studentDao().insert(mainStudent)
                    onSaveClick?.invoke(mainStudent)
                    dialog.dismiss()
                }.start()
            } else {
                Toast.makeText(context, R.string.FillData, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun fillStudentInfo() {
        val etStudentFirstName = dialogLayout.findViewById<EditText>(R.id.etName)
        val etStudentLastName = dialogLayout.findViewById<EditText>(R.id.etLastName)

        mainStudent.firstName = etStudentFirstName.text.toString()
        mainStudent.lastName = etStudentLastName.text.toString()
    }
}
