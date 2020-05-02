package pl.polsl.laboratorioweobecnosci.database.dao

import androidx.room.*
import pl.polsl.laboratorioweobecnosci.database.models.Class
import pl.polsl.laboratorioweobecnosci.database.models.Student

@Dao
interface ClassDao {
    @Insert
    fun insertAll(classObject: List<Class>)
    @Insert
    fun insert(classObject: Class):Long

    @Update
    fun update(classObject: Class)

    @Delete
    fun delete(classObject: Class)

    @Query("SELECT * FROM class")
    fun getClasses(): ArrayList<Class>

    @Query("SELECT * FROM student where class_id = :classId")
    fun getClassStudents(classId: Int): ArrayList<Student>

    @Query("SELECT workstation_id FROM student where class_id = :classId")
    fun getClassWorkstations(classId: Int): ArrayList<Int>

}
