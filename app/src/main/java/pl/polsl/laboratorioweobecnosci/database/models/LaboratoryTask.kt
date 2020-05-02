package pl.polsl.laboratorioweobecnosci.database.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey

import androidx.room.PrimaryKey
import java.util.*


@Entity
data class LaboratoryTask(
    @ColumnInfo(name = "task_number")
    val taskNumber: Int,
    val degree:Int,
    @ColumnInfo(name = "laboratory_id")
    val laboratoryId:Int,
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L
)