package pl.polsl.laboratorioweobecnosci.database.models

import androidx.room.ColumnInfo
import androidx.room.Entity

import androidx.room.PrimaryKey


@Entity
data class Workstation (
    @ColumnInfo(name = "number")
    var number: Int,
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
)