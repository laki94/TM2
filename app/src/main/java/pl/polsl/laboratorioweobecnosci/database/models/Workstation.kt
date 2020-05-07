package pl.polsl.laboratorioweobecnosci.database.models

import android.content.Context
import androidx.room.ColumnInfo
import androidx.room.Entity

import androidx.room.PrimaryKey
import pl.polsl.laboratorioweobecnosci.R


@Entity
data class Workstation (
    @ColumnInfo(name = "number")
    var number: Int,
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
) {
    fun toString(context: Context): String {
        return context.getString(R.string.WorkstationNr, number)
    }

    fun compare(workstation: Workstation): Boolean {
        return (workstation.id == id) && (workstation.number == number)
    }

    fun simpleCompare(workstation: Workstation): Boolean {
        return (workstation.number == number)
    }
}