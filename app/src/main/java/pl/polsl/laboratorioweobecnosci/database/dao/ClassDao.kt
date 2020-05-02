package pl.polsl.laboratorioweobecnosci.database.dao

import androidx.room.*
import pl.polsl.laboratorioweobecnosci.database.models.Class
import pl.polsl.laboratorioweobecnosci.database.models.Student

@Dao
interface ClassDao {
    @Insert
    fun insertAll(classObject: List<Class>)
    @Insert
    fun insert(classObject: Class)

    @Update
    fun update(classObject: Class)

    @Delete
    fun delete(classObject: Class)

    @Query("SELECT * FROM class")
    fun getClasses(): List<Class>

    @Query("SELECT * FROM student where class_id = :classId")
    fun getClassStudents(classId: Int): List<Student>
}
