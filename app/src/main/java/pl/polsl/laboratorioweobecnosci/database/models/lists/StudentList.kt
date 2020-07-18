package pl.polsl.laboratorioweobecnosci.database.models.lists

import pl.polsl.laboratorioweobecnosci.database.models.Student

/**
 * Lista studentów
 */
class StudentList: ArrayList<Student>() {

    /**
     * Pobiera informację o studentach z listy
     * @return informacje o studentach z listy
     */
    override fun toString(): String {
        return joinToString()
    }

    /**
     * Dodaje studenta na listę
     * @param student student do dodania
     */
    fun addIfNotExist(student: Student) {
        forEach {
            if (it.compare(student))
                return
        }
        add(student)
    }

    /**
     * Pobiera informację o studentach na liście w nowych liniach
     * @return informacje o studentach na liście w nowych liniach
     */
    fun toNewLineSeparatedString(): String {
        return joinToString(separator = "\n") { it.toShortString() }
    }

    /**
     * Pobiera nazwiska studentów z listy
     * @return nazwiska studentów z listy
     */
    fun toStringOnlyLastName(): String {
        return joinToString(separator = ", ") { it.lastName }
    }
}