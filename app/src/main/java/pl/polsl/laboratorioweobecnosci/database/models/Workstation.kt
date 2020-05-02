package pl.polsl.laboratorioweobecnosci.database.models

import androidx.room.ColumnInfo
import androidx.room.Entity

import androidx.room.PrimaryKey


@Entity
data class Workstation (
    @ColumnInfo(name = "number")
    val number: Int,
    @PrimaryKey(autoGenerate = true)
val id: Long = 0L
)