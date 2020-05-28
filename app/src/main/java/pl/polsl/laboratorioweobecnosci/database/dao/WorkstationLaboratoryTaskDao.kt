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

    @Query("SELECT * FROM workstationlaboratorytask WHERE workstation_id = :workstationId AND laboratory_id = :laboratoryId")
    fun getTasksDoneForWorkstationAtLaboratory(workstationId: Int, laboratoryId: Int): List<WorkstationLaboratoryTask>

    @Query("SELECT * FROM workstationlaboratorytask WHERE workstation_id = :workstationId AND laboratory_id = :labId AND laboratory_task_id = :taskId")
    fun getTaskForWorkstationAtLaboratoryWithId(workstationId: Int, labId: Int, taskId: Int): WorkstationLaboratoryTask

    @Query("SELECT * FROM workstationlaboratorytask WHERE laboratory_task_id = :taskId AND laboratory_id = :laboratoryId")
    fun getTasksWithIdAtLaboratory(taskId: Int, laboratoryId: Int): List<WorkstationLaboratoryTask>

    @Query("SELECT * FROM workstationlaboratorytask WHERE laboratory_id = :laboratoryId")
    fun getTasksDoneAtLaboratory(laboratoryId: Int): List<WorkstationLaboratoryTask>
}