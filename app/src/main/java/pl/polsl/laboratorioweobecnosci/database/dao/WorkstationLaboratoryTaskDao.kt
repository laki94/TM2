package pl.polsl.laboratorioweobecnosci.database.dao

import androidx.room.*
import pl.polsl.laboratorioweobecnosci.database.models.LaboratoryTask
import pl.polsl.laboratorioweobecnosci.database.models.StudentListWorkstationModel
import pl.polsl.laboratorioweobecnosci.database.models.WorkstationLaboratoryTask

@Dao
interface WorkstationLaboratoryTaskDao {
    @Insert
    fun insertAll(workstationClassTask: List<WorkstationLaboratoryTask>)
    @Insert
    fun insert(workstationClassTask: WorkstationLaboratoryTask):Long

    @Update
    fun update(workstationClassTask: WorkstationLaboratoryTask)

    @Delete
    fun delete(workstationClassTask: WorkstationLaboratoryTask)

    @Query("SELECT * FROM workstationlaboratorytask")
    fun getAllWorkstationLaboratoryTasks(): List<WorkstationLaboratoryTask>

    @Query("SELECT * FROM workstationlaboratorytask WHERE workstation_id = :workstationId AND laboratory_id = :laboratoryId")
    fun getTasksDoneForWorkstationAtLaboratory(workstationId: Int, laboratoryId: Int): List<WorkstationLaboratoryTask>
}