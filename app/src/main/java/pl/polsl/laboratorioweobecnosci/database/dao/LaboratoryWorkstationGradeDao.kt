package pl.polsl.laboratorioweobecnosci.database.dao

import androidx.room.*
import pl.polsl.laboratorioweobecnosci.database.models.LaboratoryWorkstationGradeModel

/**
 * Interfejs dostępu do tabeli ocen stanowiska na laboratorium
 */
@Dao
interface LaboratoryWorkstationGradeDao {

    /**
     * Włożenie do tabeli oceny dla stanowiska na laboratorium
     * @param laboratoryWorkstationGrade ocena przypisana do stanowiska na laboratorium
     * @return id zapisanej oceny dla stanowiska na laboratorium
     */
    @Insert
    fun insert(laboratoryWorkstationGrade: LaboratoryWorkstationGradeModel): Long

    /**
     * Zaktualizowanie oceny dla stanowiska na laboratorium
     * @param laboratoryWorkstationGrade ocena przypisana do stanowiska na laboratorium
     */
    @Update
    fun update(laboratoryWorkstationGrade: LaboratoryWorkstationGradeModel)

    /**
     * Usunięcie oceny dla stanowiska na laboratorium
     * @param laboratoryWorkstationGrade ocena przypisana do stanowiska na laboratorium
     */
    @Delete
    fun delete(laboratoryWorkstationGrade: LaboratoryWorkstationGradeModel)

    /**
     * Pobranie oceny dla stanowiska na laboratorium
     * @param laboratoryId Id laboratorium
     * @param workstationId Id stanowiska
     * @return ocena przypisana do stanowiska na laboratorium
     */
    @Query("SELECT * FROM laboratoryworkstationgrademodel WHERE laboratory_id = :laboratoryId AND workstation_id = :workstationId")
    fun getGradeForWorkstationAtLaboratory(laboratoryId: Int, workstationId: Int): LaboratoryWorkstationGradeModel?

    /**
     * Pobranie wszystkich ocen przypisanych do stanowisk na laboratorium
     * @param laboratoryId Id laboratorium
     * @return lista ocen przypisanych do stanowisk na laboratorium
     */
    @Query("SELECT * FROM laboratoryworkstationgrademodel WHERE laboratory_id = :laboratoryId")
    fun getGradesAtLaboratory(laboratoryId: Int): List<LaboratoryWorkstationGradeModel>
}