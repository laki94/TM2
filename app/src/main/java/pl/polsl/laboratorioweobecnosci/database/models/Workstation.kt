package pl.polsl.laboratorioweobecnosci.database.models

import android.content.Context
import androidx.room.ColumnInfo
import androidx.room.Entity

import androidx.room.PrimaryKey
import pl.polsl.laboratorioweobecnosci.R

/**
 * Stanowisko
 * @param number numer stanowiska
 * @param id Id stanowiska w tabeli
 */
@Entity
data class Workstation (
    @ColumnInfo(name = "number")
    var number: Int,
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
) {
    /**
     * Pobranie informacji o stanowisku
     * @param context context aplikacji
     * @return informacje o stanowisku
     */
    fun toString(context: Context): String {
        return context.getString(R.string.WorkstationNr, number)
    }

    /**
     * Porównanie stanowisk
     * @param workstation stanowisko do porównania
     * @return True jeśli stanowiska są takie same
     */
    fun compare(workstation: Workstation): Boolean {
        return (workstation.id == id) && (workstation.number == number)
    }

    /**
     * Proste porównanie stanowisk
     * @param workstation stanowisko do porównania
     * @return True jeśli stanowiska mają taki sam numer
     */
    fun simpleCompare(workstation: Workstation): Boolean {
        return (workstation.number == number)
    }
}