package pl.polsl.laboratorioweobecnosci.database.models.lists

import pl.polsl.laboratorioweobecnosci.database.models.LaboratoryTask
import pl.polsl.laboratorioweobecnosci.database.models.WorkstationLaboratoryTask

/**
 * Lista zadań na laboratorium
 */
class LaboratoryTaskList: ArrayList<LaboratoryTask>() {

    /**
     * Pobranie następnego wolnego numeru zadania
     * @return numer następnego wolnego zadania
     */
    fun getNextNr(): Int {
        var res = 1
        forEach {
            if (it.taskNumber >= res)
                res = it.taskNumber + 1
        }
        return res
    }

    /**
     * Ustawienie id laboratorium w zadaniach
     * @param labId Id laboratorium
     */
    fun setLaboratoryId(labId: Int) {
        forEach {
            it.laboratoryId = labId
        }
    }

    /**
     * Pobranie najwyższej możliwej oceny z zadań
     * @return najwyższa możliwa ocena z zadań
     */
    fun getHighestGrade(): Int {
        var res = 2
        forEach {
            if (it.degree > res)
                res = it.degree
        }
        return res
    }

    /**
     * Sprawdzenie czy lista posiada zadanie
     * @param laboratoryTask zadanie do porównania
     * @return True jeśli lista posiada podane zadanie
     */
    fun haveTask(laboratoryTask: LaboratoryTask): Boolean {
        forEach {
            if (it.simpleCompare(laboratoryTask))
                return true
        }
        return false
    }

    /**
     * Dodaje zadanie na listę jeśli nie było go wcześniej
     * @param laboratoryTask zadanie do dodania
     */
    fun addIfNotExist(laboratoryTask: LaboratoryTask) {
        forEach {
            if (it.simpleCompare(laboratoryTask))
                return
        }
        add(laboratoryTask)
    }
}