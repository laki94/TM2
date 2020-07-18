package pl.polsl.laboratorioweobecnosci.database.models

import android.content.Context
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey

import androidx.room.PrimaryKey
import pl.polsl.laboratorioweobecnosci.R
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*

/**
 * Laboratorium
 * @param major specjalność kierunku
 * @param laboratoryName nazwa laboratorium
 * @param semester semestr kierunku
 * @param studyType typ studiów
 * @param numberOfTasks liczba zadań na laboratorium
 * @param laboratoryStart data rozpoczęcia laboratorium
 * @param laboratoryEnd data zakończenia laboratorium
 * @param id id laboratorium w tabeli
 */
@Entity
data class Laboratory(
    var major: String,
    @ColumnInfo(name = "laboratory_name")
    var laboratoryName: String,
    var semester: Int,
    @ColumnInfo(name = "study_type")
    var studyType: String,
    @ColumnInfo(name = "number_of_tasks")
    var numberOfTasks: Int,
    @ColumnInfo(name = "laboratory_start")
    var laboratoryStart: Date,
    @ColumnInfo(name = "laboratory_end")
    var laboratoryEnd: Date,
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
) {
    /**
     * Pobranie informacji o laboratorium
     * @param context context aplikacji
     * @return informacje o laboratorium
     */
    fun toString(context: Context): String {
        return String.format("%s: %s\n%s: %d\n%s: %s",
            context.getString(R.string.Major), major, context.getString(R.string.Semester), semester,
            context.getString(R.string.StudyType), studyType)
    }

    /**
     * Pobranie rozszerzonej informacji o laboratorium
     * @param context context aplikacji
     * @return rozszerzona informacja o laboratorium
     */
    fun getInfoString(context: Context): String {
        return String.format("%s\n%s\n%s: %s\n%s: %d\n%s: %s",
            laboratoryName,
            context.getString(R.string.BeginExerciseDate, getLaboratoryStartDateString()),
            context.getString(R.string.Major), major, context.getString(R.string.Semester), semester,
            context.getString(R.string.StudyType), studyType)
    }

    /**
     * Pobranie daty rozpoczęcia laboratorium
     * @return data rozpoczęcia laboratorium
     */
    fun getLaboratoryStartDate(): LocalDateTime {
        return laboratoryStart.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()
    }

    /**
     * Pobranie daty zakończenia laboratorium
     * @return data zakończenia laboratorium
     */
    fun getLaboratoryEndDate(): LocalDateTime {
        return laboratoryEnd.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()
    }

    /**
     * Pobranie daty rozpoczęcia laboratorium
     * @return data rozpoczęcia laboratorium
     */
    fun getLaboratoryStartDateString(): String {
        return getLaboratoryStartDate()
            .format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"))
    }

    /**
     * Pobranie daty zakończenia laboratorium
     * @return data zakończenia laboratorium
     */
    fun getLaboratoryEndDateString(): String {
        return getLaboratoryEndDate()
            .format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"))
    }
}