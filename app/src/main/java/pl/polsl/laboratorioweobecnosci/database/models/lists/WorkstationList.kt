package pl.polsl.laboratorioweobecnosci.database.models.lists

import pl.polsl.laboratorioweobecnosci.database.models.Workstation

/**
 * Lista stanowisk
 */
class WorkstationList: ArrayList<Workstation>() {

    /**
     * Sprawdzenie czy lista posiada stanowisko o numerze
     * @param number numer stanowiska
     * @return True jeśli lista posiada stanowisko o podanym numerze
     */
    fun containsNr(number: Int): Boolean {
        forEach {
            if (it.number == number)
                return true
        }
        return false
    }
}