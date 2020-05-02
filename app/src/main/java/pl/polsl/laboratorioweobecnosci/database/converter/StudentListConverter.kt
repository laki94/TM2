package pl.polsl.laboratorioweobecnosci.database.converter

import androidx.room.TypeConverter
import pl.polsl.laboratorioweobecnosci.database.models.Student

class StudentListConverter {

    @TypeConverter
    fun listToArrayList(value: List<Student>?) = ArrayList<Student>(value)

    @TypeConverter
    fun jsonToList(value: ArrayList<Student>) = value.toList()
}