package pl.polsl.laboratorioweobecnosci.database.dao

import androidx.room.*
import pl.polsl.laboratorioweobecnosci.database.models.LaboratoryWorkstationGradeModel

@Dao
interface LaboratoryWorkstationGradeDao {
    @Insert
    fun insert(laboratoryWorkstationGrade: LaboratoryWorkstationGradeModel): Long

    @Update
    fun update(laboratoryWorkstationGrade: LaboratoryWorkstationGradeModel)

    @Delete
    fun delete(laboratoryWorkstationGrade: LaboratoryWorkstationGradeModel)

    @Query("SELECT * FROM laboratoryworkstationgrademodel WHERE laboratory_id = :laboratoryId AND workstation_id = :workstationId")
    fun getGradeForWorkstationAtLaboratory(laboratoryId: Int, workstationId: Int): LaboratoryWorkstationGradeModel?
}