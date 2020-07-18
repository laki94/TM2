package pl.polsl.laboratorioweobecnosci.database.models

import pl.polsl.laboratorioweobecnosci.database.models.lists.StudentList

/**
 * Lista studentów na stanowisku
 * @property students lista studentów
 * @property workstation stanowisku
 */
class StudentListWorkstationModel {
    var students = StudentList()
    var workstation = Workstation(0)

    /**
     * Pobranie informacji o studentach na stanowisku
     * @return informacja o studentach na stanowisku
     */
    override fun toString(): String {
        return String.format("%s\n%s", workstation.toString(), students.toString())
    }

}