package pl.polsl.laboratorioweobecnosci.models

class StudentsList: ArrayList<Student>() {

    override fun toString(): String {
        return joinToString()
    }
}