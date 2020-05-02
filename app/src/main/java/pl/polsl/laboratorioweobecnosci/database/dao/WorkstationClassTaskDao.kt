package pl.polsl.laboratorioweobecnosci.database.dao

import androidx.room.*
import pl.polsl.laboratorioweobecnosci.database.models.WorkstationClassTask

@Dao
interface WorkstationClassTaskDao {
    @Insert
    fun insertAll(workstationClassTask: List<WorkstationClassTask>)
    @Insert
    fun insert(workstationClassTask: WorkstationClassTask):Long

    @Update
    fun update(workstationClassTask: WorkstationClassTask)

    @Delete
    fun delete(workstationClassTask: WorkstationClassTask)

    @Query("SELECT * FROM workstationclasstask")
    fun getAllWorkstationClassTasks(): List<WorkstationClassTask>
}