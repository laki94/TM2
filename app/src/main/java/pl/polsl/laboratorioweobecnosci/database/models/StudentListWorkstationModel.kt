package pl.polsl.laboratorioweobecnosci.database.models

import android.content.Context
import pl.polsl.laboratorioweobecnosci.R
import pl.polsl.laboratorioweobecnosci.database.models.lists.StudentList

class StudentListWorkstationModel {
    lateinit var students: StudentList
    lateinit var workstation: Workstation

    override fun toString(): String {
        return String.format("%s\n%s", workstation.toString(), students.toString())
    }

}