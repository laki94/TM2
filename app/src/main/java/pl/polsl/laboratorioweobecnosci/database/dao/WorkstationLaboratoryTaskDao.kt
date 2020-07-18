package pl.polsl.laboratorioweobecnosci.database.dao

import androidx.room.*
import pl.polsl.laboratorioweobecnosci.database.models.LaboratoryTask
import pl.polsl.laboratorioweobecnosci.database.models.StudentListWorkstationModel
import pl.polsl.laboratorioweobecnosci.database.models.WorkstationLaboratoryTask

/**
 * Interfejs dostępu do tabeli zadań wykonanych przez stanowisko na laboratorium
 */
@Dao
interface WorkstationLaboratoryTaskDao {

    /**
     * Włożenie do tabeli zadań wykonanych przez stanowisko na laboratorium
     * @param workstationClassTasks lista zadań wykonanych przez stanowiska na laboratoriach
     */
    @Insert
    fun insertAll(workstationClassTasks: List<WorkstationLaboratoryTask>)

    /**
     * Włożenie do tabeli zadania wykonanego przez stanowisko na laboratorium
     * @param workstationClassTask zadanie wykonane przez stanowisko na laboratorium
     * @return id zapisanego zadania wykonanego przez stanowisko na laboratorium
     */
    @Insert
    fun insert(workstationClassTask: WorkstationLaboratoryTask):Long

    /**
     * Zaktualizowanie zadania wykonanego przez stanowisko na laboratorium
     * @param workstationClassTask zadanie wykonane przez stanowisko na laboratorium
     */
    @Update
    fun update(workstationClassTask: WorkstationLaboratoryTask)

    /**
     * Usunięcie zadania wykonanego przez stanowisko na laboratorium
     * @param workstationClassTask zadanie wykonane przez stanowisko na laboratorium
     */
    @Delete
    fun delete(workstationClassTask: WorkstationLaboratoryTask)

    /**
     * Pobranie zadań wykonanych przez stanowisko na laboratorium
     * @param workstationId Id stanowiska
     * @param laboratoryId Id laboratorium
     * @return zadań wykonanych przez stanowisko na laboratorium
     */
    @Query("SELECT * FROM workstationlaboratorytask WHERE workstation_id = :workstationId AND laboratory_id = :laboratoryId")
    fun getTasksDoneForWorkstationAtLaboratory(workstationId: Int, laboratoryId: Int): List<WorkstationLaboratoryTask>

    /**
     * Pobranie zadania wykonanego przez stanowisko na laboratorium
     * @param workstationId Id stanowska
     * @param labId Id laboratorium
     * @param taskId Id zadania
     */
    @Query("SELECT * FROM workstationlaboratorytask WHERE workstation_id = :workstationId AND laboratory_id = :labId AND laboratory_task_id = :taskId")
    fun getTaskForWorkstationAtLaboratoryWithId(workstationId: Int, labId: Int, taskId: Int): WorkstationLaboratoryTask

    /**
     * Pobranie listy wykonanych zadań na laboratorium
     * @param taskId Id zadania
     * @param laboratoryId Id laboratorium
     * @return lista wykonanych zadań z id na laboratorium
     */
    @Query("SELECT * FROM workstationlaboratorytask WHERE laboratory_task_id = :taskId AND laboratory_id = :laboratoryId")
    fun getTasksWithIdAtLaboratory(taskId: Int, laboratoryId: Int): List<WorkstationLaboratoryTask>

    /**
     * Pobranie wszystkich wykonanych zadań na laboratorium
     * @param laboratoryId Id laboratorium
     * @return lista wykonanych zadań na laboratorium
     */
    @Query("SELECT * FROM workstationlaboratorytask WHERE laboratory_id = :laboratoryId")
    fun getTasksDoneAtLaboratory(laboratoryId: Int): List<WorkstationLaboratoryTask>
}