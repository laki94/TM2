package pl.polsl.laboratorioweobecnosci.database.models

import android.content.Context
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey

import androidx.room.PrimaryKey
import pl.polsl.laboratorioweobecnosci.R
import java.util.*

/**
 * Zadanie na laboratorium
 * @param taskNumber numer zadania
 * @param degree ocena za zadanie
 * @param laboratoryId Id laboratorium
 * @param id Id zadania w tabeli
 */
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
    /**
     * Pobranie informacji o zadaniu
     * @param context context aplikacji
     * @return informacja o zadaniu
     */
    fun toString(context: Context): String {
        return context.getString(R.string.ShortTaskNr, taskNumber)
    }

    /**
     * Porównanie z innym zadaniem
     * @param laboratoryTask zadanie do porównania
     * @return True jeśli zadanie jest takie samo jak w parametrze
     */
    fun simpleCompare(laboratoryTask: LaboratoryTask): Boolean {
        return (taskNumber == laboratoryTask.taskNumber) && (degree == laboratoryTask.degree) &&
                (laboratoryId == laboratoryTask.laboratoryId)
    }
}