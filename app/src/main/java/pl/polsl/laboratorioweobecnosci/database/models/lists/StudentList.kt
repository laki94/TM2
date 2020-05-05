package pl.polsl.laboratorioweobecnosci.database.models.lists

import pl.polsl.laboratorioweobecnosci.database.models.Student

class StudentList: ArrayList<Student>() {

    override fun toString(): String { //???????
        return joinToString()
    }
}