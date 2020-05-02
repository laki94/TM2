package pl.polsl.laboratorioweobecnosci.database.models

import androidx.room.Entity


@Entity()
data class StudentWorkstationClass(
    val workstation:Workstation,
    val students: ArrayList<Student>
)