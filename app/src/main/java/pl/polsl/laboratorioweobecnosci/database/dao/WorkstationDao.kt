package pl.polsl.laboratorioweobecnosci.database.dao

import androidx.room.*
import pl.polsl.laboratorioweobecnosci.database.models.Workstation

/**
 * Interfejs dostępu do tabeli ze stanowiskami
 */
@Dao
interface WorkstationDao {

    /**
     * Włożenie stanowisk do tabeli
     * @param workstations stanowiska do zapisania
     */
    @Insert
    fun insertAll(workstations: ArrayList<Workstation>)

    /**
     * Włożenie stanowiska do tabeli
     * @param workstation stanowisko do zapisania
     * @return Id zapisanego stanowiska
     */
    @Insert
    fun insert(workstation: Workstation): Long

    /**
     * Zaktualizowanie stanowiska w tabeli
     * @param workstation stanowisko do zaktualizowania
     */
    @Update
    fun update(workstation: Workstation)

    /**
     * Usunięcie stanowiska z tabeli
     * @param workstation stanowisko do usunięcia
     */
    @Delete
    fun delete(workstation: Workstation)

    /**
     * Pobranie wszystkich stanowisk
     * @return lista stanowisk
     */
    @Query("SELECT * FROM workstation")
    fun getWorkstations(): List<Workstation>

    /**
     * Pobranie stanowiska
     * @param workstationId Id stanowiska
     * @return stanowisko z podanym id
     */
    @Query("SELECT * FROM workstation where id = :workstationId")
    fun getWorkstation(workstationId: Int): Workstation

    /**
     * Pobranie id stanowiska o podanym numerze
     * @param workstationNr numer stanowiska
     * @return id stanowiska o podanym nr
     */
    @Query("SELECT id FROM workstation where number = :workstationNr")
    fun getWorkstationId(workstationNr: Int): Int
}