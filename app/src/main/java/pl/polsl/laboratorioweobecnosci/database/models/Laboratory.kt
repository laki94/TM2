package pl.polsl.laboratorioweobecnosci.database.models

import android.content.Context
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey

import androidx.room.PrimaryKey
import pl.polsl.laboratorioweobecnosci.R
import java.time.LocalDateTime
import java.util.*


@Entity
data class Laboratory(
    var major: String,
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
}