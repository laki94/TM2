package pl.polsl.laboratorioweobecnosci.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import pl.polsl.laboratorioweobecnosci.database.converter.DateConverter
import pl.polsl.laboratorioweobecnosci.database.dao.*
import pl.polsl.laboratorioweobecnosci.database.models.*
import pl.polsl.laboratorioweobecnosci.database.models.lists.*

@Database(
    entities = [Laboratory::class, LaboratoryTask::class, Student::class, Workstation::class,
        WorkstationLaboratoryTask::class, LaboratoryWorkstationGradeModel::class],
    version = 1
)

/**
 * Uchwyt do bazy danych aplikacji
 * @property laboratoryDao dostęp do tabeli laboratoriów
 * @property laboratoryTaskDao dostęp do tabeli zadań na laboratorium
 * @property studentDao dostęp do tabeli studentów
 * @property workstationDao dostęp do tabeli stanowisk
 * @property workstationLaboratoryTaskDao dostęp do tabeli zadań wykonanych przez stanowisko na laboratorium
 * @property laboratoryWorkstationGradeDao dostęp do tabeli ocen stanowiska na laboratorium
 * @property instance zmienna używana przy budowaniu bazy danych
 * @property LOCK zmienna blokująca bazę danych
 */
@TypeConverters(DateConverter::class)
abstract class DatabaseHandler : RoomDatabase(){
    abstract fun laboratoryDao(): LaboratoryDao
    abstract fun laboratoryTaskDao(): LaboratoryTaskDao
    abstract fun studentDao(): StudentDao
    abstract fun workstationDao(): WorkstationDao
    abstract fun workstationLaboratoryTaskDao(): WorkstationLaboratoryTaskDao
    abstract fun laboratoryWorkstationGradeDao(): LaboratoryWorkstationGradeDao

    companion object {
        @Volatile private var instance: DatabaseHandler? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK){
            instance ?: buildDatabase(context).also { instance = it}
        }

        /**
         * Budowanie bazy danych
         * @param context context aplikacji
         * @return utworzona baza danych
         */
        private fun buildDatabase(context: Context) = Room.databaseBuilder(context,
            DatabaseHandler::class.java, "laboratorioweobecnosci.db")
            .addMigrations()
            .build()
    }

    /**
     * Pobranie studentów na laboratorium
     * @param labId Id laboratorium
     * @return lista studentów
     */
    fun getStudentsAtLaboratory(labId: Int): StudentList {
        val res = StudentList()
        val orgList = laboratoryDao().getLaboratoryStudents(labId)
        orgList.forEach { res.add(it) }
        return res
    }

    /**
     * Pobranie zadań na laboratorium
     * @param labId Id laboratorium
     * @return lista zadań na laboratorium
     */
    fun getTasksForLaboratory(labId: Int): LaboratoryTaskList {
        val res = LaboratoryTaskList()
        val orgList = laboratoryTaskDao().getTasksForClass(labId)
        orgList.forEach { res.add(it) }
        return res
    }

    /**
     * Pobranie studentów na stanowisku na laboratorium
     * @param labId Id laboratorium
     * @param workstationId Id stanowiska
     * @return lista studentów
     */
    fun getStudentsAtWorkstation(labId: Int, workstationId:Int): StudentList {
        val res = StudentList()
        val orgList = studentDao().getStudentsOnWorkstation(labId, workstationId)
        orgList.forEach { res.add(it) }
        return res
    }

    /**
     * Pobranie zadań wykonanych przez stanowisko na laboratorium
     * @param labId Id laboratorium
     * @param workstationId Id stanowiska
     * @return lista wykonanych zadań
     */
    fun getTasksDoneByWorkstationAtLaboratory(labId: Int, workstationId: Int): LaboratoryTaskList {
        val res = LaboratoryTaskList()
        val tasksDone = workstationLaboratoryTaskDao().getTasksDoneForWorkstationAtLaboratory(workstationId, labId)
        tasksDone.forEach {
            val task = laboratoryTaskDao().getTaskWithId(it.laboratoryTaskId)
            res.add(task)
        }
        return res
    }

    /**
     * Pobranie studentów przypisanych do stanowisk na laboratorium
     * @param labId Id laboratorium
     * @return lista studentów i stanowisk
     */
    fun getStudentsAssignedToWorkstationsAtLaboratory(labId: Int): ListOfStudentsAtWorkstation {
        val res = ListOfStudentsAtWorkstation()
        val students = getStudentsAtLaboratory(labId)
        students.forEach {
            val workstation = workstationDao().getWorkstation(it.workstationId)
            res.addStudentWorkstation(it, workstation)
        }
        return res
    }

    /**
     * Pobranie oceny stanowiska
     * @param laboratoryId Id laboratorium
     * @param workstationId Id stanowiska
     * @return ocena stanowiska na laboratorium
     */
    fun getWorkstationGrade(laboratoryId: Int, workstationId: Int): LaboratoryWorkstationGradeModel {
        var res = laboratoryWorkstationGradeDao().getGradeForWorkstationAtLaboratory(laboratoryId, workstationId)
        if (res == null) {
            res = LaboratoryWorkstationGradeModel(laboratoryId, workstationId)
        }
        return res
    }

    /**
     * Pobranie laboratoriów posortowanych po dacie startu
     * @return lista laboratoriów
     */
    fun getLaboratoriesSortedByStartDate(): LaboratoryList {
        val res = LaboratoryList()
        val list = laboratoryDao().getLaboratoriesSortedDescByStartDate()
        list.forEach {
            res.add(it)
        }
        return res
    }

    /**
     * Usunięcie zadania na laboratorium
     * @param task zadanie na laboratorium
     * @param laboratoryId Id laboratorium
     */
    fun removeTaskDoneForLaboratory(task: LaboratoryTask, laboratoryId: Int) {
        val tasksToDel = workstationLaboratoryTaskDao().getTasksWithIdAtLaboratory(task.id, laboratoryId)
        tasksToDel.forEach {
            workstationLaboratoryTaskDao().delete(it)
        }
    }

    /**
     * Usunięcie laboratorium i wszystkich powiązanych z nim danych
     * @param laboratoryId Id laboratorium
     */
    fun deleteDataWithLabId(laboratoryId: Int) {
        val tasks = laboratoryTaskDao().getTasksForClass(laboratoryId)
        tasks.forEach {
            laboratoryTaskDao().delete(it)
        }
        val students = studentDao().getStudentsAtLaboratory(laboratoryId)
        students.forEach {
            studentDao().delete(it)
        }
        val tasksDone = workstationLaboratoryTaskDao().getTasksDoneAtLaboratory(laboratoryId)
        tasksDone.forEach {
            workstationLaboratoryTaskDao().delete(it)
        }
        val grades = laboratoryWorkstationGradeDao().getGradesAtLaboratory(laboratoryId)
        grades.forEach {
            laboratoryWorkstationGradeDao().delete(it)
        }
    }
}
