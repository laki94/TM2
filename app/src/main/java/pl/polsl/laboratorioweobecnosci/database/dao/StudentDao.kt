package pl.polsl.laboratorioweobecnosci.database.dao

import androidx.room.*
import pl.polsl.laboratorioweobecnosci.database.models.Student
import pl.polsl.laboratorioweobecnosci.database.models.StudentsList

@Dao
interface StudentDao {
    @Insert
    fun insertAll(student: List<Student>)
    @Insert
    fun insert(student: Student):Long

    @Update
    fun update(student: Student)

    @Delete
    fun delete(student: Student)

    @Query("SELECT * FROM student WHERE laboratory_id = :laboratoryId and workstation_id = :workstationId")
    fun getStudentsOnWorkstation(laboratoryId: Int, workstationId:Int): List<Student>
}