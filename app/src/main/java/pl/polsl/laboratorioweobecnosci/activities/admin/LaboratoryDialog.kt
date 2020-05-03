package pl.polsl.laboratorioweobecnosci.activities.admin

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import pl.polsl.laboratorioweobecnosci.R
import pl.polsl.laboratorioweobecnosci.activities.adapters.LaboratoryTaskAdapter
import pl.polsl.laboratorioweobecnosci.database.DatabaseHandler
import pl.polsl.laboratorioweobecnosci.database.models.Laboratory
import pl.polsl.laboratorioweobecnosci.database.models.LaboratoryTask
import pl.polsl.laboratorioweobecnosci.database.models.LaboratoryTaskModel
import pl.polsl.laboratorioweobecnosci.database.models.lists.LaboratoryTaskList
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*

class LaboratoryDialog(context: Context) : AlertDialog.Builder(context) {

    private lateinit var buttonStartDate: Button
    private lateinit var buttonEndDate: Button
    private lateinit var etMajor: EditText
    private lateinit var etSemester: EditText
    private lateinit var etStudyType: EditText
    private lateinit var dialogLayout: View
    private lateinit var taskAdapter: LaboratoryTaskAdapter
    private var mainLaboratory = LaboratoryTaskModel()

    var onSaveClick: ((LaboratoryTaskModel) -> Unit)? = null

    private fun setButtonsOnClicks() {
        buttonStartDate.setOnClickListener {
            var dateFrom = mainLaboratory.laboratory.laboratoryStart.toInstant()
                .atZone(ZoneId.systemDefault()).toLocalDateTime()

            val newYear = dateFrom.year
            val newMonth = dateFrom.month.value
            val newDay = dateFrom.dayOfMonth
            val newHour = dateFrom.hour
            val newMinute = dateFrom.minute

            val timePickerDialog = TimePickerDialog(context,
                TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
                    val newTime = LocalTime.of(hourOfDay, minute)
                    mainLaboratory.laboratory.laboratoryStart = Date.from(
                        LocalDateTime.of(dateFrom.toLocalDate(), newTime)
                            .atZone(ZoneId.systemDefault()).toInstant())
                    fillInfo()
                }, newHour, newMinute, true)

            val datePickerDialog = DatePickerDialog(context,
                DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                    val newDate = LocalDate.of(year, month, dayOfMonth)
                    dateFrom = LocalDateTime.of(newDate, dateFrom.toLocalTime())
                    timePickerDialog.show()
                }, newYear, newMonth, newDay)
            datePickerDialog.show()
        }

        buttonEndDate.setOnClickListener {
            var dateTo = mainLaboratory.laboratory.laboratoryEnd.toInstant()
                .atZone(ZoneId.systemDefault()).toLocalDateTime()

            val newYear = dateTo.year
            val newMonth = dateTo.month.value
            val newDay = dateTo.dayOfMonth
            val newHour = dateTo.hour
            val newMinute = dateTo.minute

            val timePickerDialog = TimePickerDialog(context,
                TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
                    val newTime = LocalTime.of(hourOfDay, minute)
                    mainLaboratory.laboratory.laboratoryEnd =
                        Date.from(LocalDateTime.of(dateTo.toLocalDate(), newTime)
                            .atZone(ZoneId.systemDefault()).toInstant())
                    fillInfo()
                }, newHour, newMinute, true)

            val datePickerDialog = DatePickerDialog(context,
                DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                    val newDate = LocalDate.of(year, month, dayOfMonth)
                    dateTo = LocalDateTime.of(newDate, dateTo.toLocalTime())
                    timePickerDialog.show()
                }, newYear, newMonth, newDay)
            datePickerDialog.show()
        }
    }

    private fun fillLaboratory() {
        mainLaboratory.laboratory.major = etMajor.text.toString()
        if (etSemester.text.toString().isEmpty())
            mainLaboratory.laboratory.semester = 0
        else
            mainLaboratory.laboratory.semester = etSemester.text.toString().toInt()
        mainLaboratory.laboratory.studyType = etStudyType.text.toString()
        mainLaboratory.laboratory.numberOfTasks = mainLaboratory.tasks.count()
    }

    private fun fillInfo() {
        buttonStartDate = dialogLayout.findViewById(R.id.bExerciseStartDate)
        buttonEndDate = dialogLayout.findViewById(R.id.bExerciseEndDate)
        etMajor = dialogLayout.findViewById(R.id.etMajor)
        etSemester = dialogLayout.findViewById(R.id.etSemester)
        etStudyType = dialogLayout.findViewById(R.id.etStudyType)

        val dateFrom = mainLaboratory.laboratory.laboratoryStart.toInstant()
            .atZone(ZoneId.systemDefault()).toLocalDateTime()
        val dateTo = mainLaboratory.laboratory.laboratoryEnd.toInstant()
            .atZone(ZoneId.systemDefault()).toLocalDateTime()
        buttonStartDate.text = context.getString(R.string.BeginExerciseDate,
            dateFrom.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")))

        buttonEndDate.text =
            context.getString(R.string.EndExerciseDate,
                dateTo.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")))

        etMajor.setText(mainLaboratory.laboratory.major)
        if (mainLaboratory.laboratory.semester == 0)
            etSemester.setText("")
        else
            etSemester.setText(mainLaboratory.laboratory.semester.toString())
        etStudyType.setText(mainLaboratory.laboratory.studyType)
    }

    fun showEditDialog(inflater: LayoutInflater, laboratory: Laboratory, tasks: LaboratoryTaskList) {
        dialogLayout = inflater.inflate(R.layout.dialog_laboratory, null)

        mainLaboratory.laboratory = laboratory
        mainLaboratory.tasks = tasks

        setTaskAdapter()
        fillInfo()
        setButtonsOnClicks()

        setTitle(R.string.EditExercise)
        setView(dialogLayout)
        setPositiveButton(R.string.Save) { _, _ ->
            fillLaboratory()
            onSaveClick?.invoke(mainLaboratory)
        }
        super.show()
    }

    private fun setTaskAdapter() {
        taskAdapter = LaboratoryTaskAdapter(context, mainLaboratory.tasks)
        taskAdapter.let {
            it.onRemoveClick = {
                taskAdapter.removeItem(taskAdapter.itemCount - 1)
                if (taskAdapter.itemCount > 0)
                    taskAdapter.refreshItem(taskAdapter.itemCount - 1)
            }
        }

        val rvList = dialogLayout.findViewById<RecyclerView>(R.id.rvTasks)
        rvList.layoutManager = LinearLayoutManager(context)
        rvList.adapter = taskAdapter

        val bAddTask = dialogLayout.findViewById<Button>(R.id.bAddTask)
        bAddTask.setOnClickListener {
            taskAdapter.addNewItem(LaboratoryTask(mainLaboratory.tasks.getNextNr(),
                2, mainLaboratory.laboratory.id.toInt()))
            if (taskAdapter.itemCount > 1)
                taskAdapter.refreshItem(taskAdapter.itemCount - 2)
        }
    }

    fun showAddDialog(inflater: LayoutInflater) {
        dialogLayout = inflater.inflate(R.layout.dialog_laboratory, null)

        mainLaboratory.laboratory = Laboratory("", 0, "", 0,
            Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()),
            Date.from(LocalDateTime.now().plusHours(1).plusMinutes(30)
                .atZone(ZoneId.systemDefault()).toInstant()))

        mainLaboratory.tasks = LaboratoryTaskList()

        setTaskAdapter()
        fillInfo()
        setButtonsOnClicks()

        setTitle(R.string.AddExercise)
        setView(dialogLayout)
        setPositiveButton(R.string.Save) { _, _ ->
            fillLaboratory()
            onSaveClick?.invoke(mainLaboratory)

//            Thread {
//                val db = DatabaseHandler(context)
//                db.laboratoryTaskDao().insertAll(list)
//                List<LaboratoryTask>(0)
//                laboratoryTasks.iterator().forEachRemaining {

//                }
//            }
        }
        super.show()
    }
}
