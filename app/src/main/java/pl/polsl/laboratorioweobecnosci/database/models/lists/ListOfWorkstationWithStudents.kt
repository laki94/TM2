package pl.polsl.laboratorioweobecnosci.database.models.lists

import pl.polsl.laboratorioweobecnosci.database.models.Student
import pl.polsl.laboratorioweobecnosci.database.models.StudentListWorkstationModel
import pl.polsl.laboratorioweobecnosci.database.models.Workstation

/**
 * Lista stanowisk ze studentami
 */
class ListOfWorkstationsWithStudents: ArrayList<StudentListWorkstationModel>() {

    /**
     * Dodanie studenta do stanowiska
     * @param student student do dodania
     * @param workstation stanowisko, do którego ma zostać dodany student
     */
    fun addStudentToWorkstation(student: Student, workstation: Workstation) {
        forEach {
            if (it.workstation.number == workstation.number) {
                it.students.add(student)
                return
            }
        }
        val newObj = StudentListWorkstationModel()
        newObj.workstation = workstation
        newObj.students.add(student)
        add(newObj)
    }
}