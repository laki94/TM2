package pl.polsl.laboratorioweobecnosci.database.models

import android.content.Context
import pl.polsl.laboratorioweobecnosci.R
import pl.polsl.laboratorioweobecnosci.database.models.lists.LaboratoryTaskList

class StudentWorkstationModel {
    lateinit var student: Student
    lateinit var workstation: Workstation

    fun toString(context: Context): String {
        return String.format("%s: %s\n%s: %s\n%s: %d",
        context.getString(R.string.FirstName), student.firstName,
        context.getString(R.string.LastName), student.lastName,
        context.getString(R.string.Workstation), workstation.number)
    }
}