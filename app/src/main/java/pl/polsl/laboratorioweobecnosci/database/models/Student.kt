package pl.polsl.laboratorioweobecnosci.database.models

import androidx.room.*
import pl.polsl.laboratorioweobecnosci.database.converter.StudentListConverter


@Entity
data class Student(
    @ColumnInfo(name = "first_name")
    val firstName: String,
    @ColumnInfo(name = "last_name")
    val lastName: String,
    @ColumnInfo(name = "laboratory_id")
    val laboratoryId:Int,
    @ColumnInfo(name = "workstation_id")
    val workstationId:Int,
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L
)