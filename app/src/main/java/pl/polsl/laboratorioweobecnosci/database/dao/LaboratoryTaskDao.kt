package pl.polsl.laboratorioweobecnosci.database.dao

import androidx.room.*
import pl.polsl.laboratorioweobecnosci.database.models.LaboratoryTask

@Dao
interface LaboratoryTaskDao {
    @Insert
    fun insertAll(laboratoryTask: List<LaboratoryTask>)
    @Insert
    fun insert(laboratoryTask: LaboratoryTask):Long

    @Update
    fun update(laboratoryTask: LaboratoryTask)

    @Delete
    fun delete(laboratoryTask: LaboratoryTask)

    @Query("SELECT * FROM laboratorytask")
    fun getAllClassTasks(): List<LaboratoryTask>

    @Query("SELECT * FROM laboratorytask where laboratory_id = :classId")
    fun getTasksForClass(classId: Int): List<LaboratoryTask>
}