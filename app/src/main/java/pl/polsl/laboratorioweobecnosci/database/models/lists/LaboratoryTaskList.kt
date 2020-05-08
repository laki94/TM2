package pl.polsl.laboratorioweobecnosci.database.models.lists

import pl.polsl.laboratorioweobecnosci.database.models.LaboratoryTask
import pl.polsl.laboratorioweobecnosci.database.models.WorkstationLaboratoryTask

class LaboratoryTaskList: ArrayList<LaboratoryTask>() {

    fun getNextNr(): Int {
        var res = 1
        forEach {
            if (it.taskNumber >= res)
                res = it.taskNumber + 1
        }
        return res
    }

    fun setLaboratoryId(labId: Int) {
        forEach {
            it.laboratoryId = labId
        }
    }

    fun getHighestGrade(): Int {
        var res = 2
        forEach {
            if (it.degree > res)
                res = it.degree
        }
        return res
    }

    fun haveTask(workstationLaboratoryTask: WorkstationLaboratoryTask): Boolean {
        forEach {
            if ((workstationLaboratoryTask.laboratoryId == it.laboratoryId) && (workstationLaboratoryTask.laboratoryTaskId == it.id))
                return true
        }
        return false
    }

    fun haveTask(laboratoryTask: LaboratoryTask): Boolean {
        forEach {
            if (it.simpleCompare(laboratoryTask))
                return true
        }
        return false
    }

    fun addIfNotExist(laboratoryTask: LaboratoryTask) {
        forEach {
            if (it.simpleCompare(laboratoryTask))
                return
        }
        add(laboratoryTask)
    }
}