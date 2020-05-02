package pl.polsl.laboratorioweobecnosci.database.models

import androidx.room.ColumnInfo
import androidx.room.Entity

import androidx.room.PrimaryKey

@Entity
data class WorkstationClassTask (
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    @ColumnInfo(name = "class_task_id")
    val classTaskId:Int,
    @ColumnInfo(name = "workstation_id")
    val workstationId:Int
)