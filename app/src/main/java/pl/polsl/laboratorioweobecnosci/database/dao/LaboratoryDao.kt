package pl.polsl.laboratorioweobecnosci.database.dao

import androidx.room.*
import pl.polsl.laboratorioweobecnosci.database.models.Laboratory
import pl.polsl.laboratorioweobecnosci.database.models.Student

@Dao
interface LaboratoryDao {
    @Insert
    fun insertAll(laboratory: List<Laboratory>)
    @Insert
    fun insert(laboratory: Laboratory):Long

    @Update
    fun update(laboratory: Laboratory)

    @Delete
    fun delete(laboratory: Laboratory)

    @Query("SELECT * FROM laboratory")
    fun getLaboratories(): List<Laboratory>

    @Query("SELECT * FROM student where laboratory_id = :laboratoryId")
    fun getLaboratoryStudents(laboratoryId: Int): List<Student>

    @Query("SELECT workstation_id FROM student where laboratory_id = :laboratoryId group by workstation_id")
    fun getLaboratoryWorkstations(laboratoryId: Int): List<Int>

}
