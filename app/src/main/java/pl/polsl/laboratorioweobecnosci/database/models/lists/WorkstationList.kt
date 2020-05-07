package pl.polsl.laboratorioweobecnosci.database.models.lists

import pl.polsl.laboratorioweobecnosci.database.models.Workstation

class WorkstationList: ArrayList<Workstation>() {

    fun containsNr(number: Int): Boolean {
        forEach {
            if (it.number == number)
                return true
        }
        return false
    }
}