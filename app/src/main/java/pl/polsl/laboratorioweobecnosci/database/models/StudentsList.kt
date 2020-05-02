package pl.polsl.laboratorioweobecnosci.database.models

class StudentsList(studentsOnWorkstation: List<Student>) : ArrayList<Student>() {
    override fun toString(): String {
        return joinToString()
    }
}