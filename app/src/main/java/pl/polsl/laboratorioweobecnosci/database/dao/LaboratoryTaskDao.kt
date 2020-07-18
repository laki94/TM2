package pl.polsl.laboratorioweobecnosci.database.dao

import androidx.room.*
import pl.polsl.laboratorioweobecnosci.database.models.LaboratoryTask
import pl.polsl.laboratorioweobecnosci.database.models.lists.LaboratoryTaskList

/**
 * Interfejs dostępu do tabeli zadań na laboratorium w bazie
 */
@Dao
interface LaboratoryTaskDao {

    /**
     * Włożenie listy zadań na laboratorium do tabeli
     * @param laboratoryTask lista zadań do zapisania
     */
    @Insert
    fun insertAll(laboratoryTask: List<LaboratoryTask>)

    /**
     * Włożenie zadania na laboratorium do tabeli
     * @param laboratoryTask zadanie do zapisania
     * @return id zapisanego zadania
     */
    @Insert
    fun insert(laboratoryTask: LaboratoryTask):Long

    /**
     * Zaktualizowanie zadania na laboratorium w tabeli
     * @param laboratoryTask zadanie do zaktualizowania
     */
    @Update
    fun update(laboratoryTask: LaboratoryTask)

    /**
     * Zaktualizowanie zadań na laboratorium w tabeli
     * @param laboratoryTasks lista zadań do zaktualizowania
     */
    @Update
    fun updateAll(laboratoryTasks: List<LaboratoryTask>)

    /**
     * Usunięcie zadania na laboratorium z tabeli
     * @param laboratoryTask zadanie do usunięcia
     */
    @Delete
    fun delete(laboratoryTask: LaboratoryTask)

    /**
     * Usunięcie zadań, które są przypisane to laboratorium
     * @param laboratoryId Id laboratorium
     */
    @Query("DELETE FROM laboratorytask WHERE laboratory_id = :laboratoryId")
    fun deleteTaskWithLabId(laboratoryId: Int)

    /**
     * Pobranie wszystkich zadań
     * @return listę zadań na wszystkich laboratoriach
     */
    @Query("SELECT * FROM laboratorytask")
    fun getAllClassTasks(): List<LaboratoryTask>

    /**
     * Pobranie zadań przypisanych do laboratorium
     * @param classId Id laboratorium
     * @return lista zadań przypisanych do laboratorium
     */
    @Query("SELECT * FROM laboratorytask where laboratory_id = :classId")
    fun getTasksForClass(classId: Int): List<LaboratoryTask>

    /**
     * Pobranie zadania z podanym id
     * @param taskId Id zadania
     * @return zadanie na laboratorium
     */
    @Query("SELECT * FROM laboratorytask where id = :taskId")
    fun getTaskWithId(taskId: Int): LaboratoryTask
}