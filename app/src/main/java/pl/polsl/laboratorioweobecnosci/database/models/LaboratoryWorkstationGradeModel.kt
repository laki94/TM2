package pl.polsl.laboratorioweobecnosci.database.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Ocena przypisana do stanowiska na laboratorium
 * @param laboratoryId Id laboratorium
 * @param workstationId Id stanowiska
 * @param grade ocena
 * @param id Id modelu w tabeli
 */
@Entity
data class LaboratoryWorkstationGradeModel (
    @ColumnInfo(name = "laboratory_id")
    val laboratoryId: Int,
    @ColumnInfo(name = "workstation_id")
    val workstationId: Int,
    @ColumnInfo(name = "grade")
    var grade: Int = 2,
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
) {

    /**
     * Funkcja zwracająca kopię bieżącego obiektu
     * @return Kopia aktualnego obiektu
     */
    fun clone(): LaboratoryWorkstationGradeModel {
        return LaboratoryWorkstationGradeModel(laboratoryId, workstationId, grade, id)
    }
}