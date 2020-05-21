package pl.polsl.laboratorioweobecnosci.database.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

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
)