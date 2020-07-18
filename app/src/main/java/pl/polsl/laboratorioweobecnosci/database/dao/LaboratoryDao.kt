package pl.polsl.laboratorioweobecnosci.database.dao

import androidx.room.*
import pl.polsl.laboratorioweobecnosci.database.models.Laboratory
import pl.polsl.laboratorioweobecnosci.database.models.Student

/**
 * Interfejs dostępu do tabeli laboratoriów w bazie
 */
@Dao
interface LaboratoryDao {

    /**
     * Włożenie listy laboratoriów do tabeli
     * @param laboratory lista laboratoriów do zapisania
     */
    @Insert
    fun insertAll(laboratory: List<Laboratory>)

    /**
     * Włożenie laboratorium do tabeli
     * @param laboratory laboratorium do zapisania
     */
    @Insert
    fun insert(laboratory: Laboratory):Long

    /**
     * Zaktualizowanie laboratorium w tabeli
     * @param laboratory laboratorium do zaktualizowania
     */
    @Update
    fun update(laboratory: Laboratory)

    /**
     * Usunięcie laboratorium z tabeli
     * @param laboratory laboratorium do usunięcia
     */
    @Delete
    fun delete(laboratory: Laboratory)

    /**
     * Pobranie wszystkich laboratoriów z tabeli
     * @return lista laboratoriów
     */
    @Query("SELECT * FROM laboratory")
    fun getLaboratories(): List<Laboratory>

    /**
     * Pobranie studentów uczestniczących w laboratorium
     * @param laboratoryId Id laboratorium
     * @return lista studentów na laboratorium
     */
    @Query("SELECT * FROM student where laboratory_id = :laboratoryId")
    fun getLaboratoryStudents(laboratoryId: Int): List<Student>

    /**
     * Pobranie stanowisk używanych na laboratorium
     * @param laboratoryId Id laboratorium
     * @return lista numerów używanych stanowisk
     */
    @Query("SELECT workstation_id FROM student where laboratory_id = :laboratoryId group by workstation_id")
    fun getLaboratoryWorkstations(laboratoryId: Int): List<Int>

    /**
     * Pobranie laboratorium z podanym id
     * @param laboratoryId Id laboratorium
     * @return laboratorium
     */
    @Query("SELECT * FROM laboratory WHERE id = :laboratoryId")
    fun getLaboratory(laboratoryId: Int): Laboratory

    /**
     * Pobranie laboratoriów posortowanych malejąco po dacie rozpoczęcia
     * @return lista laboratoriów posortowanych malejąco po dacie rozpoczęcia
     */
    @Query("SELECT * FROM laboratory ORDER BY laboratory_start DESC")
    fun getLaboratoriesSortedDescByStartDate(): List<Laboratory>
}
