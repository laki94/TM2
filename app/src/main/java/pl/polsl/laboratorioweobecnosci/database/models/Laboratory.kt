package pl.polsl.laboratorioweobecnosci.database.models

import android.content.Context
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey

import androidx.room.PrimaryKey
import pl.polsl.laboratorioweobecnosci.R
import java.util.*


@Entity
data class Laboratory(
    val major: String,
    val semester: Int,
    @ColumnInfo(name = "study_type")
    val studyType: String,
    @ColumnInfo(name = "number_of_tasks")
    val numberOfTasks: Int,
    @ColumnInfo(name = "laboratory_start")
    val laboratoryStart: Date,
    @ColumnInfo(name = "laboratory_end")
    val laboratoryEnd: Date,
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L
) {
    fun toString(context: Context): String {
        return String.format("%s: %s\n%s: %d\n%s: %s",
            context.getString(R.string.Major), major, context.getString(R.string.Semester), semester,
            context.getString(R.string.StudyType), studyType)
    }
}