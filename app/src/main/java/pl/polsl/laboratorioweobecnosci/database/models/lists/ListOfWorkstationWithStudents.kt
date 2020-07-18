package pl.polsl.laboratorioweobecnosci.database.models.lists

import pl.polsl.laboratorioweobecnosci.database.models.Student
import pl.polsl.laboratorioweobecnosci.database.models.StudentListWorkstationModel
import pl.polsl.laboratorioweobecnosci.database.models.Workstation

/**
 * Lista stanowisk ze studentami
 */
class ListOfWorkstationsWithStudents: ArrayList<StudentListWorkstationModel>() {

    /**
     * Pobranie listy stanowisk ze studentami posortowana po numerze stanowiska
     * @return lista stanowisk ze studentami posortowana po numerze stanowiska
     */
    fun getSortedItemsByWorkstation(): ListOfWorkstationsWithStudents {
        val list = sortedWith(compareBy { it.workstation.number })
        val res = ListOfWorkstationsWithStudents()
        list.iterator().forEachRemaining {
            res.add(it)
        }
        return res
    }

    /**
     * Pobranie listy stanowisk posortowanych po numerze stanowiska
     * @return lista stanowisk posortowanych po numerze stanowiska
     */
    fun getSortedWorkstation(): WorkstationList {
        val workstations = WorkstationList()
        forEach {
            if (!workstations.containsNr(it.workstation.number))
                workstations.add(it.workstation)
        }
        val list = workstations.sortedWith(compareBy { it.number })
        workstations.clear()
        list.iterator().forEachRemaining {
            workstations.add(it)
        }
        return workstations
    }

    /**
     * Dodanie studenta do stanowiska
     * @param student student do dodania
     * @param workstation stanowisko, do którego ma zostać dodany student
     */
    fun addStudentToWorkstation(student: Student, workstation: Workstation) {
        forEach{
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

    /**
     * Pobranie studentów będących na stanowisku
     * @param workstation stanowisko
     * @return lista studentów będących na stanowisku lub null jeśli nie ma takiego stanowiska
     */
    fun getStudentsAtWorkstation(workstation: Workstation): StudentList? {
        forEach {
            if (it.workstation.number == workstation.number) {
                return it.students
            }
        }
        return null
    }
}