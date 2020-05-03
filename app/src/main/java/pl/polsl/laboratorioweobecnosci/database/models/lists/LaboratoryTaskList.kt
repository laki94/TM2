package pl.polsl.laboratorioweobecnosci.database.models.lists

import pl.polsl.laboratorioweobecnosci.database.models.LaboratoryTask

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
}