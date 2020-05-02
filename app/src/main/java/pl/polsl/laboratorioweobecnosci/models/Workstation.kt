package pl.polsl.laboratorioweobecnosci.models

import android.content.Context
import pl.polsl.laboratorioweobecnosci.R

class Workstation(workstationNr: Int) {
    private val mNr = workstationNr
    private val mStudents = StudentsList()

    fun toString(context: Context): String {
        return String.format("%s %d:\n%s", context.getString(R.string.Workstation), mNr, mStudents.toString())
    }

    fun stationNr(): Int {
        return mNr
    }

    fun addStudent(student: Student) {
        mStudents.add(student)
    }

    fun removeStudent(student: Student) {
        mStudents.remove(student)
    }

    fun students(): StudentsList {
        return mStudents
    }
}