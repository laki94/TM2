package pl.polsl.laboratorioweobecnosci.database.models

import pl.polsl.laboratorioweobecnosci.database.models.lists.LaboratoryTaskList

class LaboratoryTaskModel {
    lateinit var laboratory: Laboratory
    lateinit var tasks: LaboratoryTaskList
}