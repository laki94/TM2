package pl.polsl.laboratorioweobecnosci.database.dao

import androidx.room.*
import pl.polsl.laboratorioweobecnosci.database.models.Student

@Dao
interface StudentDao {
    @Insert
    fun insertAll(student: ArrayList<Student>)
    @Insert
    fun insert(student: Student):Long

    @Update
    fun update(student: Student)

    @Delete
    fun delete(student: Student)

    @Query("SELECT * FROM student WHERE class_id = :classId and workstation_id = :workstationId")
    fun getStudentsOnWorkstation(classId: Int, workstationId:Int):ArrayList<Student>
}