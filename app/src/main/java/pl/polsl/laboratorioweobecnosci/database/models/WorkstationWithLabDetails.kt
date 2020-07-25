package pl.polsl.laboratorioweobecnosci.database.models

import pl.polsl.laboratorioweobecnosci.database.models.lists.LaboratoryTaskList

/**
 * Informacja o stanowisku, studentach na nim, zadaniach wykonanych, do wykonania i przypisanej ocenie
 * @property workstationWithStudents stanowisko ze studentami
 * @property tasksDone zadania wykonane
 * @property tasksToDo zadania do wykonania
 * @property forcedGrade przypisana ocena
 */
class WorkstationWithLabDetails {
    lateinit var workstationWithStudents: StudentListWorkstationModel
    lateinit var tasksDone: LaboratoryTaskList
    lateinit var tasksToDo: LaboratoryTaskList
    var forcedGrade: LaboratoryWorkstationGradeModel? = null

    /**
     * Funkcja informująca czy wszystkie zadania zostały wykonane
     * @return True jeśli liczba zadań wykonanych jest równa zadaniom do wykonania
     */
    fun haveAllTasksDone(): Boolean {
        return tasksDone.count() == tasksToDo.count()
    }
}