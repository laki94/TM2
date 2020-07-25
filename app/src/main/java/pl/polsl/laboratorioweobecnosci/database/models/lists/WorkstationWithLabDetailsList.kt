package pl.polsl.laboratorioweobecnosci.database.models.lists

import pl.polsl.laboratorioweobecnosci.database.models.LaboratoryWorkstationGradeModel
import pl.polsl.laboratorioweobecnosci.database.models.StudentListWorkstationModel
import pl.polsl.laboratorioweobecnosci.database.models.WorkstationWithLabDetails

/**
 * Lista informacji o stanowisku, studentach na nim, zadaniach wykonanych, do wykonania i przypisanej ocenie na laboratorium
 */
class WorkstationWithLabDetailsList: ArrayList<WorkstationWithLabDetails>() {

    /**
     * Dodaje rekord od listy
     * @param workstationsWithStudents stanowisko ze studentami
     * @param allTasks zadania do wykonania
     * @param tasksDone zadania wykonane
     * @param grade ocena przypisana do stanowiska
     */
    fun addItem(workstationsWithStudents: StudentListWorkstationModel,
                allTasks: LaboratoryTaskList, tasksDone: LaboratoryTaskList,
                grade: LaboratoryWorkstationGradeModel?
    ) {
        val newItem = WorkstationWithLabDetails()
        newItem.workstationWithStudents = workstationsWithStudents
        newItem.tasksToDo = allTasks
        newItem.tasksDone = tasksDone
        newItem.forcedGrade = grade
        add(newItem)
    }

    /**
     * Sortowanie obiektów na liście po numerze stanowiska
     */
    fun sortByWorkstationNr() {
        this.sortBy {
            it.workstationWithStudents.workstation.number
        }
    }
}