package pl.polsl.laboratorioweobecnosci.database.models

import androidx.room.ColumnInfo
import androidx.room.Entity

import androidx.room.PrimaryKey

@Entity
data class WorkstationLaboratoryTask (
    @ColumnInfo(name = "laboratory_task_id")
    val laboratoryTaskId:Int,
    @ColumnInfo(name = "workstation_id")
    val workstationId:Int,
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L
)