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
import pl.polsl.laboratorioweobecnosci.database.models.lists.StudentWorkstationLaboratoryList
import pl.polsl.laboratorioweobecnosci.database.models.lists.StudentList

@Database(
//    entities = [Class::class, ClassTask::class, Student::class, Workstation::class, WorkstationClassTask::class],
    entities = [Laboratory::class, LaboratoryTask::class, Student::class, Workstation::class, WorkstationLaboratoryTask::class],
    version = 1

)
@TypeConverters(DateConverter::class, StudentListConverter::class)
abstract class DatabaseHandler : RoomDatabase(){
    abstract fun laboratoryDao(): LaboratoryDao
    abstract fun laboratoryTaskDao(): LaboratoryTaskDao
    abstract fun studentDao(): StudentDao
    abstract fun workstationDao(): WorkstationDao
    abstract fun workstationLaboratoryTaskDao(): WorkstationLaboratoryTaskDao

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
    fun getWorkstationsWithStudents(laboratoryId:Int): StudentWorkstationLaboratoryList {
        var workstations = this.laboratoryDao().getLaboratoryWorkstations(laboratoryId)
        var workstationStudent =
            StudentWorkstationLaboratoryList()
        workstations.forEach {
            workstationStudent.add(
                StudentWorkstationLaboratory(
                    this.workstationDao().getWorkstation(it),
                    StudentList(
                        this.studentDao().getStudentsOnWorkstation(laboratoryId, it)
                    )
                )
            )
        }
        return workstationStudent
    }
}
