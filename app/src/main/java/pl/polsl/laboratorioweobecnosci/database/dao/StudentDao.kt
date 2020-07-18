package pl.polsl.laboratorioweobecnosci.database.dao

import androidx.room.*
import pl.polsl.laboratorioweobecnosci.database.models.Student

/**
 * Interfejs dostępu do tabeli studentów
 */
@Dao
interface StudentDao {

    /**
     * Włożenie studentów do tabeli
     * @param students lista studentów do zapisania
     */
    @Insert
    fun insertAll(students: List<Student>)

    /**
     * Włożenie studenta do tabeli
     * @param student student do zapisania
     * @return Id zapisanego studenta
     */
    @Insert
    fun insert(student: Student):Long

    /**
     * Zaktualizowanie studenta w tabeli
     * @param student student do zaktualizowania
     */
    @Update
    fun update(student: Student)

    /**
     * Usunięcie studenta z tabeli
     * @param student student do usunięcia
     */
    @Delete
    fun delete(student: Student)

    /**
     * Pobranie studentów przypisanych do stanowiska
     * @param laboratoryId Id laboratorium
     * @param workstationId Id stanowiska
     * @return lista studentów na stanowisku
     */
    @Query("SELECT * FROM student WHERE laboratory_id = :laboratoryId and workstation_id = :workstationId")
    fun getStudentsOnWorkstation(laboratoryId: Int, workstationId:Int): List<Student>

    /**
     * Pobranie studentów na laboratorium
     * @param laboratoryId Id laboratorium
     * @return lista studentów na laboratorium
     */
    @Query("SELECT * FROM student WHERE laboratory_id = :laboratoryId")
    fun getStudentsAtLaboratory(laboratoryId: Int): List<Student>
}