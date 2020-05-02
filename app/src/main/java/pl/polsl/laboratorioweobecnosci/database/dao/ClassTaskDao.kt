package pl.polsl.laboratorioweobecnosci.database.dao

import androidx.room.*
import pl.polsl.laboratorioweobecnosci.database.models.ClassTask

@Dao
interface ClassTaskDao {
    @Insert
    fun insertAll(classTask: List<ClassTask>)
    @Insert
    fun insert(classTask: ClassTask)

    @Update
    fun update(classTask: ClassTask)

    @Delete
    fun delete(classTask: ClassTask)

    @Query("SELECT * FROM classtask")
    fun getAllClassTasks(): List<ClassTask>

    @Query("SELECT * FROM classtask where class_id = :classId")
    fun getTasksForClass(classId: Int): List<ClassTask>
}