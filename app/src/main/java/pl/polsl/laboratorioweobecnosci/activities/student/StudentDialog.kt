package pl.polsl.laboratorioweobecnosci.activities.student

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import pl.polsl.laboratorioweobecnosci.R
import pl.polsl.laboratorioweobecnosci.database.DatabaseHandler
import pl.polsl.laboratorioweobecnosci.database.models.LaboratoryTaskModel
import pl.polsl.laboratorioweobecnosci.database.models.Student
import pl.polsl.laboratorioweobecnosci.database.models.StudentWorkstationModel
import pl.polsl.laboratorioweobecnosci.database.models.Workstation

class StudentDialog(context: Context) : AlertDialog.Builder(context) {

    var onSaveClick: ((StudentWorkstationModel) -> Unit)? = null

    private var mainStudentWorkstation = StudentWorkstationModel()

    private lateinit var dialogLayout: View

    fun addStudent(layoutInflater: LayoutInflater, laboratoryId: Int) {
        dialogLayout = layoutInflater.inflate(R.layout.activity_student, null)
        mainStudentWorkstation.student = Student("", "", laboratoryId,0)
        mainStudentWorkstation.workstation = Workstation(0)
        fillInfo()
        setTitle(R.string.AddStudent)
        setView(dialogLayout)
        setPositiveButton(R.string.Save) { _, _ -> }

        val dialog = create()
        dialog.show()

        val btn = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
        btn.setOnClickListener {
            fillStudentInfo()
            if (mainStudentWorkstation.student.firstName.isNotEmpty() && mainStudentWorkstation.student.lastName.isNotEmpty() && (mainStudentWorkstation.workstation.number != 0)) {
                val confirmDialog = AlertDialog.Builder(context)
                val confirmDialogLayout = layoutInflater.inflate(R.layout.card_item, null)
                confirmDialog.setTitle(R.string.IsDataValid)
                confirmDialog.setPositiveButton(R.string.Yes) { _, _ ->
                    onSaveClick?.invoke(mainStudentWorkstation)
                    dialog.dismiss()
                }
                confirmDialog.setNeutralButton(R.string.Cancel) { _, _ -> }
                confirmDialog.setView(confirmDialogLayout)
                val tvStudentInfo = confirmDialogLayout.findViewById<TextView>(R.id.tvItem)
                tvStudentInfo.text = mainStudentWorkstation.toString(context)
                confirmDialog.show()
            } else {
                Toast.makeText(context, R.string.FillData, Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun editStudent(student: Student, workstation: Workstation, layoutInflater: LayoutInflater) {
        dialogLayout = layoutInflater.inflate(R.layout.activity_student, null)
        mainStudentWorkstation.student = student
        mainStudentWorkstation.workstation = workstation
        mainStudentWorkstation.workstation.id = 0

        fillInfo()
        setTitle(R.string.EditStudent)
        setView(dialogLayout)
        setPositiveButton(R.string.Save) { _, _ -> }

        val dialog = create()
        dialog.show()

        val btn = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
        btn.setOnClickListener {
            fillStudentInfo()
            if (mainStudentWorkstation.student.firstName.isNotEmpty() && mainStudentWorkstation.student.lastName.isNotEmpty() && (mainStudentWorkstation.workstation.number != 0)) {
                onSaveClick?.invoke(mainStudentWorkstation)
                dialog.dismiss()
            } else {
                Toast.makeText(context, R.string.FillData, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun fillInfo() {
        val etFirstName = dialogLayout.findViewById<EditText>(R.id.etName)
        val etLastName = dialogLayout.findViewById<EditText>(R.id.etLastName)
        val etWorkstationNr = dialogLayout.findViewById<EditText>(R.id.etWorkstation)

        etFirstName.setText(mainStudentWorkstation.student.firstName)
        etLastName.setText(mainStudentWorkstation.student.lastName)
        if (mainStudentWorkstation.workstation.number == 0) {
            etWorkstationNr.setText("")
        } else {
            etWorkstationNr.setText(mainStudentWorkstation.workstation.number.toString())
        }
    }

    private fun fillStudentInfo() {
        val etStudentFirstName = dialogLayout.findViewById<EditText>(R.id.etName)
        val etStudentLastName = dialogLayout.findViewById<EditText>(R.id.etLastName)
        val etWorkstationNr = dialogLayout.findViewById<EditText>(R.id.etWorkstation)

        mainStudentWorkstation.student.firstName = etStudentFirstName.text.toString()
        mainStudentWorkstation.student.lastName = etStudentLastName.text.toString()

        if (etWorkstationNr.text.toString().toIntOrNull() == null) {
            mainStudentWorkstation.workstation.number = 0
        } else {
            mainStudentWorkstation.workstation.number = etWorkstationNr.text.toString().toInt()
        }
    }
}
