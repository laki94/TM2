package pl.polsl.laboratorioweobecnosci.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Update
import pl.polsl.laboratorioweobecnosci.database.models.Student

@Dao
interface StudentDao {
    @Insert
    fun insertAll(student: List<Student>)
    @Insert
    fun insert(student: Student)

    @Update
    fun update(student: Student)

    @Delete
    fun delete(student: Student)


}