package pl.polsl.laboratorioweobecnosci.csv

import android.content.Context
import android.widget.Toast
import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVPrinter
import pl.polsl.laboratorioweobecnosci.R
import pl.polsl.laboratorioweobecnosci.database.DatabaseHandler
import pl.polsl.laboratorioweobecnosci.preferences.PermissionsManager
import pl.polsl.laboratorioweobecnosci.preferences.PreferencesManager
import java.io.File
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class CsvGenerator(context: Context, onGenerated: (isSuccess: Boolean, message: String) -> Unit) {

    private val mainContext = context
    private val doOnGenerated = onGenerated

    fun generate(laboratoryId: Int) {
        Thread {
            try {
                val db = DatabaseHandler(mainContext)
                val studentListWorkstation =
                    db.getStudentsAssignedToWorkstationsAtLaboratory(laboratoryId)

                studentListWorkstation.sortByWorkstationNr()
                val allTasks = db.getTasksForLaboratory(laboratoryId)
                val csvFileName = LocalDateTime.now().atZone(ZoneId.systemDefault()).format(
                    DateTimeFormatter.ofPattern("yyyy-MM-dd_HH:mm:ss")
                ) + ".csv"

                val csvDir = File(PreferencesManager.instance.saveCSVPath())
                csvDir.mkdirs()
                val writer = File(csvDir, csvFileName).bufferedWriter()
                writer.use {
                    val csvPrinter = CSVPrinter(it, CSVFormat.DEFAULT
                        .withHeader(mainContext.getString(R.string.Workstation),
                            mainContext.getString(R.string.FirstName),
                            mainContext.getString(R.string.LastName),
                            mainContext.getString(R.string.Tasks),
                            mainContext.getString(R.string.Grade)))

                    studentListWorkstation.forEach { studentWorkstation ->
                        val tasksDone = db.getTasksDoneByWorkstationAtLaboratory(
                            laboratoryId,
                            studentWorkstation.workstation.id
                        )
                        val gradeModel = db.getWorkstationGrade(
                            laboratoryId,
                            studentWorkstation.workstation.id
                        )

                        val boolTasks = ArrayList<Boolean>()
                        allTasks.forEach { task ->
                            boolTasks.add(tasksDone.haveTask(task))
                        }

                        csvPrinter.printRecord(
                            studentWorkstation.workstation.number,
                            studentWorkstation.student.firstName,
                            studentWorkstation.student.lastName,
                            boolTasks,
                            gradeModel.grade
                        )
                    }
                    csvPrinter.flush()
                    csvPrinter.close()
                    doOnGenerated(true, csvFileName)
                }
            } catch (e: Exception) {
                doOnGenerated(false, e.toString())
            }
        }.start()
    }
}