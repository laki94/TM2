package pl.polsl.laboratorioweobecnosci.database.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class LaboratoryStudentGradeModel (
    @ColumnInfo(name = "laboratory_id")
    val laboratoryId: Int,
    @ColumnInfo(name = "student_id")
    val studentId: Int,
    @ColumnInfo(name = "grade")
    var grade: Int,
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0
)