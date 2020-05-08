package pl.polsl.laboratorioweobecnosci.database.models.lists

import pl.polsl.laboratorioweobecnosci.database.models.Student
import pl.polsl.laboratorioweobecnosci.database.models.StudentListWorkstationModel
import pl.polsl.laboratorioweobecnosci.database.models.Workstation

class ListOfWorkstationsWithStudents: ArrayList<StudentListWorkstationModel>() {

    fun sortedWorkstationPosition(workstationNr: Int): Int {
        val workstations = getSortedWorkstation()
        for (pos in 0 until workstations.count()) {
            if (workstations[pos].number == workstationNr) {
                return pos
            }
        }
        return -1
    }

    fun getSortedItemsByWorkstation(): ListOfWorkstationsWithStudents {
        val list = sortedWith(compareBy { it.workstation.number })
        val res = ListOfWorkstationsWithStudents()
        list.iterator().forEachRemaining {
            res.add(it)
        }
        return res
    }

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

    fun getStudentsAtWorkstation(workstation: Workstation): StudentList? {
        forEach {
            if (it.workstation.number == workstation.number) {
                return it.students
            }
        }
        return null
    }
}