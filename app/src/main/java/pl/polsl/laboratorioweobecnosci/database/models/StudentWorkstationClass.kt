package pl.polsl.laboratorioweobecnosci.database.models

import android.content.Context
import androidx.room.Entity
import pl.polsl.laboratorioweobecnosci.R


@Entity()
data class StudentWorkstationClass(
    val workstation:Workstation,
    val students: StudentsList
) {
    fun toString(context: Context): String {
        return String.format("%s: %d\n%s", context.getString(R.string.Workstation),
            workstation.number, students.toString())
    }
}