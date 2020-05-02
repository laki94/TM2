package pl.polsl.laboratorioweobecnosci.database.models

class StudentsList: ArrayList<Student>() {

    override fun toString(): String {
        return joinToString()
    }
}