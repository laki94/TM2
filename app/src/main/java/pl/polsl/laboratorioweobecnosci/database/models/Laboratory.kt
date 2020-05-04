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
    var id: Long = 0L
) {
    fun toString(context: Context): String {
        return String.format("%s: %s\n%s: %d\n%s: %s",
            context.getString(R.string.Major), major, context.getString(R.string.Semester), semester,
            context.getString(R.string.StudyType), studyType)
    }

    fun getInfoString(context: Context): String {
        return String.format("%s\n%s\n%s: %s\n%s: %d\n%s: %s",
            laboratoryName,
            context.getString(R.string.BeginExerciseDate, getLaboratoryStartDateString()),
            context.getString(R.string.Major), major, context.getString(R.string.Semester), semester,
            context.getString(R.string.StudyType), studyType)
    }

    fun getLaboratoryStartDate(): LocalDateTime {
        return laboratoryStart.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()
    }

    fun getLaboratoryEndDate(): LocalDateTime {
        return laboratoryEnd.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()
    }

    fun getLaboratoryStartDateString(): String {
        return getLaboratoryStartDate()
            .format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"))
    }

    fun getLaboratoryEndDateString(): String {
        return getLaboratoryEndDate()
            .format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"))
    }
}