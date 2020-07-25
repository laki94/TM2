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

/**
 * Klasa generująca plik CSV
 * @param mainContext context wywołującej aktywności
 * @param doOnGenerated callback wywołany po zakończeniu generowania pliku
 */
class CsvGenerator(private val mainContext: Context, private val doOnGenerated: (isSuccess: Boolean, message: String) -> Unit) {

    /**
     * funkcja generująca plik CSV dla laboratorium
     * @param laboratoryId Id laboratorium
     */
    fun generate(laboratoryId: Int) {
        Thread {
            try {
                val db = DatabaseHandler(mainContext)
                val labInfo = db.getAllWorkstationsDetails(laboratoryId)
                labInfo.sortByWorkstationNr()

                val csvFileName = LocalDateTime.now().atZone(ZoneId.systemDefault()).format(
                    DateTimeFormatter.ofPattern("yyyy-MM-dd_HH:mm:ss")
                ) + ".csv"

                val csvDir = File(PreferencesManager.getInstance(mainContext).saveCSVPath())
                csvDir.mkdirs()
                val writer = File(csvDir, csvFileName).bufferedWriter()
                writer.use {
                    val csvPrinter = CSVPrinter(it, CSVFormat.DEFAULT
                        .withHeader(mainContext.getString(R.string.Workstation),
                            mainContext.getString(R.string.FirstName),
                            mainContext.getString(R.string.LastName),
                            mainContext.getString(R.string.Tasks),
                            mainContext.getString(R.string.Grade)))

                    labInfo.forEach { workstationWithStudentsDetails ->
                        workstationWithStudentsDetails.workstationWithStudents.students.forEach { student ->
                            csvPrinter.printRecord(
                                workstationWithStudentsDetails.workstationWithStudents.workstation.number,
                                student.firstName,
                                student.lastName,
                                workstationWithStudentsDetails.getTasksDoneAsBools(),
                                workstationWithStudentsDetails.getGrade()
                            )
                        }
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