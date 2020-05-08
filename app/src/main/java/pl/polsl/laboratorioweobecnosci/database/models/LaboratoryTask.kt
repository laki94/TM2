package pl.polsl.laboratorioweobecnosci.database.models

import android.content.Context
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey

import androidx.room.PrimaryKey
import pl.polsl.laboratorioweobecnosci.R
import java.util.*


@Entity
data class LaboratoryTask(
    @ColumnInfo(name = "task_number")
    var taskNumber: Int,
    var degree: Int,
    @ColumnInfo(name = "laboratory_id")
    var laboratoryId: Int,
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0
) {
    fun toString(context: Context): String {
        return context.getString(R.string.ShortTaskNr, taskNumber)
    }

    fun simpleCompare(laboratoryTask: LaboratoryTask): Boolean {
        return (taskNumber == laboratoryTask.taskNumber) && (degree == laboratoryTask.degree) &&
                (laboratoryId == laboratoryTask.laboratoryId)
    }
}