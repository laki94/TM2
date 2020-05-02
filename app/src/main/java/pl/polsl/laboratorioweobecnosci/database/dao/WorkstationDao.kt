package pl.polsl.laboratorioweobecnosci.database.dao

import androidx.room.*
import pl.polsl.laboratorioweobecnosci.database.models.Workstation

@Dao
interface WorkstationDao {
    @Insert
    fun insertAll(workstation: ArrayList<Workstation>)
    @Insert
    fun insert(workstation: Workstation):Long

    @Update
    fun update(workstation: Workstation)

    @Delete
    fun delete(workstation: Workstation)

    @Query("SELECT * FROM workstation")
    fun getWorkstations(): List<Workstation>

    @Query("SELECT * FROM workstation where id = :workstationId")
    fun getWorkstation(workstationId: Int): Workstation
}