package pl.polsl.laboratorioweobecnosci.database.models

import androidx.room.*
import pl.polsl.laboratorioweobecnosci.database.converter.StudentListConverter


@Entity
data class Student(
    @ColumnInfo(name = "first_name")
    var firstName: String,
    @ColumnInfo(name = "last_name")
    var lastName: String,
    @ColumnInfo(name = "laboratory_id")
    var laboratoryId:Int,
    @ColumnInfo(name = "workstation_id")
    var workstationId:Int,
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0
) {
    fun toShortString(): String {
        return String.format("%s %s", firstName, lastName)
    }

    fun compare(student: Student): Boolean {
        return (id == student.id) && (firstName == student.firstName) &&
                (lastName == student.lastName) && (laboratoryId == student.laboratoryId) &&
                (workstationId == student.workstationId)
    }

    fun simpleCompare(student: Student): Boolean {
        return (firstName == student.firstName) && (lastName == student.lastName) &&
                (laboratoryId == student.laboratoryId) && (workstationId == student.workstationId)
    }
}