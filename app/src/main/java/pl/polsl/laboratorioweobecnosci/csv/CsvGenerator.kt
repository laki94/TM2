package pl.polsl.laboratorioweobecnosci.csv

import android.content.Context
import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVPrinter
import pl.polsl.laboratorioweobecnosci.database.DatabaseHandler
import java.io.File
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class CsvGenerator(context: Context) {

    private val mainContext = context

    fun generate(laboratoryId: Int) {
        Thread {
            val db = DatabaseHandler(mainContext)
            val studentListWorkstation = db.getStudentsAssignedToWorkstationsAtLaboratory(laboratoryId)
            val allTasks = db.getTasksForLaboratory(laboratoryId)
            val csvFileName = LocalDateTime.now().atZone(ZoneId.systemDefault()).format(
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + ".csv"

            val csvDir = File(mainContext.filesDir.path + "/csv/")
            csvDir.mkdirs()
            val writer = File(csvDir, csvFileName).bufferedWriter()
            writer.use {
                val csvPrinter = CSVPrinter(it, CSVFormat.DEFAULT)

                studentListWorkstation.forEach { studentWorkstation ->
                    val tasksDone = db.getTasksDoneByWorkstationAtLaboratory(
                        laboratoryId,
                        studentWorkstation.workstation.id
                    )
                    val gradeModel = db.getWorkstationGrade(laboratoryId,
                        studentWorkstation.workstation.id)

                    val boolTasks = ArrayList<Boolean>()
                    allTasks.forEach { task ->
                        boolTasks.add(tasksDone.haveTask(task))
                    }

                    csvPrinter.printRecord(
                        studentWorkstation.workstation.number,
                        studentWorkstation.student.firstName,
                        studentWorkstation.student.lastName,
                        boolTasks,
                        gradeModel?.grade
                    )
                }
                csvPrinter.flush()
                csvPrinter.close()
            }
        }.start()
    }
}