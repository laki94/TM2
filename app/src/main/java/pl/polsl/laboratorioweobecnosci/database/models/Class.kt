package pl.polsl.laboratorioweobecnosci.database.models

import android.content.Context
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey

import androidx.room.PrimaryKey
import pl.polsl.laboratorioweobecnosci.R
import java.util.*


@Entity()
data class Class(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val major: String,
    val semester: Int,
    @ColumnInfo(name = "study_type")
    val studyType: String,
    @ColumnInfo(name = "number_of_tasks")
    val numberOfTasks: Int,
    @ColumnInfo(name = "class_start")
    val classStart: Date,
    @ColumnInfo(name = "class_end")
    val classEnd: Date
) {
    fun toString(context: Context): String {
        return String.format("%s: %s\n%s: %d\n%s: %s",
            context.getString(R.string.Major), major, context.getString(R.string.Semester), semester,
            context.getString(R.string.StudyType), studyType)
    }
}