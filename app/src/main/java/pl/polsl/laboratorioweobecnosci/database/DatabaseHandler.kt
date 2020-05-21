package pl.polsl.laboratorioweobecnosci.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import pl.polsl.laboratorioweobecnosci.database.converter.DateConverter
import pl.polsl.laboratorioweobecnosci.database.converter.StudentListConverter
import pl.polsl.laboratorioweobecnosci.database.dao.*
import pl.polsl.laboratorioweobecnosci.database.models.*
import pl.polsl.laboratorioweobecnosci.database.models.lists.*

@Database(
    entities = [Laboratory::class, LaboratoryTask::class, Student::class, Workstation::class,
        WorkstationLaboratoryTask::class, LaboratoryWorkstationGradeModel::class],
    version = 1
)
@TypeConverters(DateConverter::class, StudentListConverter::class)
abstract class DatabaseHandler : RoomDatabase(){
    abstract fun laboratoryDao(): LaboratoryDao
    abstract fun laboratoryTaskDao(): LaboratoryTaskDao
    abstract fun studentDao(): StudentDao
    abstract fun workstationDao(): WorkstationDao
    abstract fun workstationLaboratoryTaskDao(): WorkstationLaboratoryTaskDao
    abstract fun laboratoryGradeDao(): LaboratoryWorkstationGradeDao

    companion object {
        @Volatile private var instance: DatabaseHandler? = null
        private val LOCK = Any()

        operator fun invoke(context: Context)= instance ?: synchronized(LOCK){
            instance ?: buildDatabase(context).also { instance = it}
        }

        private fun buildDatabase(context: Context) = Room.databaseBuilder(context,
            DatabaseHandler::class.java, "laboratorioweobecnosci.db")
            .addMigrations()
            .build()
    }

    fun getAllLaboratories(): LaboratoryList {
        val res = LaboratoryList()
        val orgList = laboratoryDao().getLaboratories()
        orgList.forEach { res.add(it) }
        return res
    }

    fun getStudentsAtLaboratory(labId: Int): StudentList {
        val res = StudentList()
        val orgList = laboratoryDao().getLaboratoryStudents(labId)
        orgList.forEach { res.add(it) }
        return res
    }

    fun getAllLaboratoryTasks(): LaboratoryTaskList {
        val res = LaboratoryTaskList()
        val orgList = laboratoryTaskDao().getAllClassTasks()
        orgList.forEach { res.add(it) }
        return res
    }

    fun getTasksForLaboratory(labId: Int): LaboratoryTaskList {
        val res = LaboratoryTaskList()
        val orgList = laboratoryTaskDao().getTasksForClass(labId)
        orgList.forEach { res.add(it) }
        return res
    }

    fun getStudentsAtWorkstation(labId: Int, workstationId:Int): StudentList {
        val res = StudentList()
        val orgList = studentDao().getStudentsOnWorkstation(labId, workstationId)
        orgList.forEach { res.add(it) }
        return res
    }

    fun getAllWorkstations(): WorkstationList {
        val res = WorkstationList()
        val orgList = workstationDao().getWorkstations()
        orgList.forEach { res.add(it) }
        return res
    }

    fun getTasksDoneByWorkstationAtLaboratory(labId: Int, workstationId: Int): LaboratoryTaskList {
        val res = LaboratoryTaskList()
        val tasksDone = workstationLaboratoryTaskDao().getTasksDoneForWorkstationAtLaboratory(workstationId, labId)
        tasksDone.forEach {
            val task = laboratoryTaskDao().getTaskWithId(it.laboratoryTaskId)
            res.add(task)
        }
        return res
    }

    fun getStudentsAssignedToWorkstationsAtLaboratory(labId: Int): ListOfStudentsAtWorkstation {
        val res = ListOfStudentsAtWorkstation()
        val students = getStudentsAtLaboratory(labId)
        students.forEach {
            val workstation = workstationDao().getWorkstation(it.workstationId)
            res.addStudentWorkstation(it, workstation)
        }
        return res
    }


//    fun getWorkstationsWithStudents(laboratoryId:Int): StudentWorkstationLaboratoryList {// ????
//        var workstations = this.laboratoryDao().getLaboratoryWorkstations(laboratoryId)
//        var workstationStudent =
//            StudentWorkstationLaboratoryList()
//        workstations.forEach {
//            var workstation = this.workstationDao().getWorkstation(it)
//            var list = this.studentDao().getStudentsOnWorkstation(laboratoryId, it)
//            var newStudentWorkstation = StudentWorkstationLaboratory(workstation, StudentList())
//
//            list.iterator().forEachRemaining { student ->
//                newStudentWorkstation.students.add(student)
//            }
//            workstationStudent.add(newStudentWorkstation)
//        }
//        return workstationStudent
//    }
}
