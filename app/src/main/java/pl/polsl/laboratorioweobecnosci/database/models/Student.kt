package pl.polsl.laboratorioweobecnosci.database.models

import androidx.room.*

/**
 * Student
 * @param firstName Imię studenta
 * @param lastName Nazwisko studenta
 * @param laboratoryId Id laboratorium
 * @param workstationId Id stanowiska
 * @param id Id modelu w tabeli
 */
@Entity
data class Student(
    @ColumnInfo(name = "first_name")
    var firstName: String,
    @ColumnInfo(name = "last_name")
    var lastName: String,
    @ColumnInfo(name = "laboratory_id")
    var laboratoryId:Int,
    @ColumnInfo(name = "workstation_id")
    var workstationId:Int,
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0
) {
    /**
     * Pobranie informacji o studencie
     * @return informacja o studencie
     */
    fun toShortString(): String {
        return String.format("%s %s", firstName, lastName)
    }

    /**
     * Porównanie z innym studentem
     * @param student student z którym będzie porównanie
     * @return True jeśli studenci są tacy sami
     */
    fun compare(student: Student): Boolean {
        return (id == student.id) && (firstName == student.firstName) &&
                (lastName == student.lastName) && (laboratoryId == student.laboratoryId) &&
                (workstationId == student.workstationId)
    }

    /**
     * Porównanie z innym studentem
     * @param student student z którym będzie porównanie
     * @return True jeśli podstawowe dane się zgadzają (bez id)
     */
    fun simpleCompare(student: Student): Boolean {
        return (firstName == student.firstName) && (lastName == student.lastName) &&
                (laboratoryId == student.laboratoryId) && (workstationId == student.workstationId)
    }
}