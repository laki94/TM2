package pl.polsl.laboratorioweobecnosci.database.models

import pl.polsl.laboratorioweobecnosci.database.models.lists.LaboratoryTaskList

/**
 * Laboratorium z zadaniami
 * @property laboratory laboratorium
 * @property tasks zadania wykonania na laboratorium
 */
class LaboratoryTaskModel {
    lateinit var laboratory: Laboratory
    lateinit var tasks: LaboratoryTaskList
}