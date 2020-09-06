package pl.polsl.laboratorioweobecnosci.database.models.lists

import pl.polsl.laboratorioweobecnosci.database.models.Student
import pl.polsl.laboratorioweobecnosci.database.models.StudentWorkstationModel
import pl.polsl.laboratorioweobecnosci.database.models.Workstation

/**
 * Lista studentów na stanowiskach
 */
class ListOfStudentsAtWorkstation: ArrayList<StudentWorkstationModel>() {

    /**
     * Pobiera liczbę używanych stanowisk
     * @return liczba używanych stanowisk
     */
    fun getWorkstationsCount(): Int {
        val workstations = ArrayList<Int>()
        forEach {
            if (!workstations.contains(it.workstation.number))
                workstations.add(it.workstation.number)
        }
        return workstations.size
    }

    /**
     * Pobiera studentów przy stanowisku
     * @param workstationNr numer stanowiska
     * @return lista studentów przy stanowisku
     */
    fun getStudentsAtWorkstationNr(workstationNr: Int): StudentList {
        val res = StudentList()
        forEach {
            if (it.workstation.number == workstationNr)
                res.addIfNotExist(it.student)
        }
        return res
    }

    /**
     * Pobranie pozycji na liście stanowiska
     * @param workstationNr numer stanowiska
     * @return pozycja stanowiska na liście
     */
    fun sortedWorkstationPosition(workstationNr: Int): Int {
        val sortedWorkstations = getSortedWorkstation()

        for (pos in 0 until sortedWorkstations.count()) {
            if (sortedWorkstations[pos].number == workstationNr) {
                return pos
            }
        }
        return -1
    }

    /**
     * Pobranie listy stanowisk posortowanej po numerze stanowiska
     * @return lista stanowisk
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
     * Edycja studenta na liście
     * @param studentWorkstation student na stanowisku
     */
    fun editStudent(studentWorkstation: StudentWorkstationModel) {
        forEach {
            if (it.student.id == studentWorkstation.student.id) {
                it.student = studentWorkstation.student
                it.workstation = studentWorkstation.workstation
                return
            }
        }
    }

    /**
     * Usunięcie studenta z listy
     * @param studentWorkstation student na stanowisku
     */
    fun removeStudent(studentWorkstation: StudentWorkstationModel) {
        forEach {
            if (it.compare(studentWorkstation)) {
                remove(it)
                return
            }
        }
    }

    /**
     * Dodanie student na stanowisku na listę
     * @param student student do dodania
     * @param workstation stanowisko do dodania
     */
    fun addStudentWorkstation(student: Student, workstation: Workstation) {
        forEach {
            if ((student.compare(it.student)) && (workstation.number == it.workstation.number))
                return
        }
        val newObj =
            StudentWorkstationModel()
        newObj.student = student
        newObj.workstation = workstation
        add(newObj)
    }
}