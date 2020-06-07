package pl.polsl.laboratorioweobecnosci.database.models

import pl.polsl.laboratorioweobecnosci.database.models.lists.StudentList
import pl.polsl.laboratorioweobecnosci.database.models.lists.WorkstationList

class ListOfStudentsAtWorkstation: ArrayList<StudentWorkstationModel>() {

    fun getWorkstationsCount(): Int {
        val workstations = ArrayList<Int>()
        forEach {
            if (!workstations.contains(it.workstation.number))
                workstations.add(it.workstation.number)
        }
        return workstations.size
    }

    fun getStudentsAtWorkstationNr(workstationNr: Int): StudentList {
        val res = StudentList()
        forEach {
            if (it.workstation.number == workstationNr)
                res.addIfNotExist(it.student)
        }
        return res
    }

    fun sortByWorkstationNr() {
        this.sortBy {
            it.workstation.number
        }
    }

    fun sortedWorkstationPosition(workstationNr: Int): Int {
        val sortedWorkstations = getSortedWorkstation()

        for (pos in 0 until sortedWorkstations.count()) {
            if (sortedWorkstations[pos].number == workstationNr) {
                return pos
            }
        }
        return -1
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

    fun editStudent(studentWorkstation: StudentWorkstationModel) {
        forEach {
            if (it.student.id == studentWorkstation.student.id) {
                it.student = studentWorkstation.student
                it.workstation = studentWorkstation.workstation
                return
            }
        }
    }

    fun removeStudent(studentWorkstation: StudentWorkstationModel) {
        forEach {
            if (it.compare(studentWorkstation)) {
                remove(it)
                return
            }
        }
    }

    fun addStudentWorkstation(student: Student, workstation: Workstation) {
        forEach {
            if ((student.compare(it.student)) && (workstation.number == it.workstation.number))
                return
        }
        val newObj = StudentWorkstationModel()
        newObj.student = student
        newObj.workstation = workstation
        add(newObj)
    }
}