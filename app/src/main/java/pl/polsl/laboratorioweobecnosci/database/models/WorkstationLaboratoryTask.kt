package pl.polsl.laboratorioweobecnosci.database.models

import androidx.room.ColumnInfo
import androidx.room.Entity

import androidx.room.PrimaryKey

/**
 * Zadanie wykonane przez stanowisko na laboratorium
 * @param laboratoryTaskId Id zadania
 * @param workstationId Id stanowiska
 * @param laboratoryId Id laboratorium
 * @param id Id modelu w tabeli
 */
@Entity
data class WorkstationLaboratoryTask (
    @ColumnInfo(name = "laboratory_task_id")
    val laboratoryTaskId: Int,
    @ColumnInfo(name = "workstation_id")
    var workstationId: Int,
    @ColumnInfo(name = "laboratory_id")
    val laboratoryId: Int,
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
)