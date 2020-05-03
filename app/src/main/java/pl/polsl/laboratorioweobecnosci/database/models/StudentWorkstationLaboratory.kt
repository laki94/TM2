package pl.polsl.laboratorioweobecnosci.database.models

import android.content.Context
import pl.polsl.laboratorioweobecnosci.R
import pl.polsl.laboratorioweobecnosci.database.models.lists.StudentList

data class StudentWorkstationLaboratory(
    val workstation:Workstation,
    val students: StudentList
) {
    fun toString(context: Context): String {
        return String.format("%s: %d\n%s", context.getString(R.string.Workstation),
            workstation.number, students.toString())
    }
}