package pl.polsl.laboratorioweobecnosci.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import pl.polsl.laboratorioweobecnosci.database.dao.*
import pl.polsl.laboratorioweobecnosci.database.models.*
import java.lang.Class

@Database(
    entities = [Class::class, ClassTask::class, Student::class, Workstation::class, WorkstationClassTask::class],
    version = 1
)
//@TypeConverters(DateConverter::class)
abstract class DatabaseHandler : RoomDatabase(){
    abstract fun classDao(): ClassDao
    abstract fun classTaskDao(): ClassTaskDao
    abstract fun studentDao(): StudentDao
    abstract fun workstationDao(): WorkstationDao
    abstract fun workstationClassTaskDao(): WorkstationClassTaskDao

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
    fun getWorkstationsWithStudents(classId:Int): ArrayList<StudentWorkstationClass> {
        var workstations = this.classDao().getClassWorkstations(classId)
        var workstationStudent = ArrayList<StudentWorkstationClass>()
        workstations.forEach {
            workstationStudent.add(
                StudentWorkstationClass(
                    this.workstationDao().getWorkstation(it),
                    this.studentDao().getStudentsOnWorkstation(classId, it)
                )
            )
        }
        return workstationStudent
    }
}
