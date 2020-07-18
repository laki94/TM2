package pl.polsl.laboratorioweobecnosci.database.models

import android.content.Context
import pl.polsl.laboratorioweobecnosci.R
import pl.polsl.laboratorioweobecnosci.database.models.lists.LaboratoryTaskList

/**
 * Student przypisany do stanowiska
 * @property student student
 * @property workstation stanowisko
 */
class StudentWorkstationModel {
    lateinit var student: Student
    lateinit var workstation: Workstation

    /**
     * Pobranie informacji o studencie na stanowisku
     * @param context context aplikacji
     * @return informacja o studencie na stanowisku
     */
    fun toString(context: Context): String {
        return String.format("%s: %s\n%s: %s\n%s: %d",
        context.getString(R.string.FirstName), student.firstName,
        context.getString(R.string.LastName), student.lastName,
        context.getString(R.string.Workstation), workstation.number)
    }

    /**
     * Porównanie obiektu z obiektem z parametru
     * @param studentWorkstation obiekt do porównania
     * @return True jeśli obiekt jest taki sam jak ten w parametrze
     */
    fun compare(studentWorkstation: StudentWorkstationModel): Boolean {
        return (student.simpleCompare(studentWorkstation.student) &&
                workstation.simpleCompare(studentWorkstation.workstation))
    }

    /**
     * Stworzenie klona obiektu
     * @return klon obiektu
     */
    fun clone(): StudentWorkstationModel {
        val res = StudentWorkstationModel()
        res.workstation = Workstation(workstation.number, workstation.id)
        res.student = Student(student.firstName, student.lastName, student.laboratoryId,
            student.workstationId, student.id)
        return res
    }
}