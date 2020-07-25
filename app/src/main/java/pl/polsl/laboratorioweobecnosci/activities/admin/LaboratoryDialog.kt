package pl.polsl.laboratorioweobecnosci.activities.admin

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.ScrollView
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat.getSystemService
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

/**
 * Dialog służący do edycji i dodawania pojedynczego laboratorium
 * @param context context aktywności
 * @property buttonStartDate Przycisk z datą rozpoczęcia laboratorium
 * @property buttonEndDate Przycisk z datą zakończenia laboratorium
 * @property etLaboratoryName Nazwa laboratorium
 * @property etMajor Specjalność kierunku
 * @property etSemester Semestr kierunku
 * @property etStudyType Typ kierunku
 * @property dialogLayout dialog, który zostanie wyświetlony
 * @property taskAdapter adapter przypisany do RecyclerView dialogu
 * @property mainLaboratory edytowane/dodawane laboratorium
 * @property onSaveClick Callback, który zostanie wywołany po wciśnięciu przycisku zapisz
 */
class LaboratoryDialog(context: Context) : AlertDialog.Builder(context) {

    private lateinit var buttonStartDate: Button
    private lateinit var buttonEndDate: Button
    private lateinit var etLaboratoryName: EditText
    private lateinit var etMajor: EditText
    private lateinit var etSemester: EditText
    private lateinit var etStudyType: EditText
    private lateinit var dialogLayout: View
    private lateinit var taskAdapter: LaboratoryTaskAdapter
    private var mainLaboratory = LaboratoryTaskModel()

    var onSaveClick: ((LaboratoryTaskModel) -> Unit)? = null

    /**
     * Przypisanie funkcji onClick przycisków wyboru dat w dialogu
     */
    private fun setButtonsOnClicks() {
        buttonStartDate.setOnClickListener {
            var dateFrom = mainLaboratory.laboratory.getLaboratoryStartDate()

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

                    buttonStartDate.text = context.getString(R.string.BeginExerciseDate,
                        mainLaboratory.laboratory.getLaboratoryStartDateString())

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
            var dateTo = mainLaboratory.laboratory.getLaboratoryEndDate()

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

                    buttonEndDate.text =
                        context.getString(R.string.EndExerciseDate,
                            mainLaboratory.laboratory.getLaboratoryEndDateString())

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

    /**
     * Uzupełnienie dodawanego/edytowanego laboratorium danymi z interfejsu
     */
    private fun fillLaboratory() {
        mainLaboratory.laboratory.major = etMajor.text.toString()
        if (etSemester.text.toString().isEmpty())
            mainLaboratory.laboratory.semester = 0
        else
            mainLaboratory.laboratory.semester = etSemester.text.toString().toInt()
        mainLaboratory.laboratory.studyType = etStudyType.text.toString()
        mainLaboratory.laboratory.numberOfTasks = mainLaboratory.tasks.count()
        mainLaboratory.laboratory.laboratoryName = etLaboratoryName.text.toString()
    }

    /**
     *  Uzupełnienie interfejsu danymi z dodawanego/edytowanego laboratorium
     */
    private fun fillInfo() {
        buttonStartDate = dialogLayout.findViewById(R.id.bExerciseStartDate)
        buttonEndDate = dialogLayout.findViewById(R.id.bExerciseEndDate)
        etMajor = dialogLayout.findViewById(R.id.etMajor)
        etSemester = dialogLayout.findViewById(R.id.etSemester)
        etStudyType = dialogLayout.findViewById(R.id.etStudyType)
        etLaboratoryName = dialogLayout.findViewById(R.id.etLaboratoryName)

        buttonStartDate.text = context.getString(R.string.BeginExerciseDate,
            mainLaboratory.laboratory.getLaboratoryStartDateString())

        buttonEndDate.text =
            context.getString(R.string.EndExerciseDate,
                mainLaboratory.laboratory.getLaboratoryEndDateString())

        etMajor.setText(mainLaboratory.laboratory.major)
        if (mainLaboratory.laboratory.semester == 0)
            etSemester.setText("")
        else
            etSemester.setText(mainLaboratory.laboratory.semester.toString())
        etStudyType.setText(mainLaboratory.laboratory.studyType)
        etLaboratoryName.setText(mainLaboratory.laboratory.laboratoryName)
    }

    /**
     * Wyświetlenie dialogu z edycją laboratorium
     * @param inflater LayoutInflater aktywności wywołującej
     * @param laboratory edytowane laboratorium
     * @param tasks zadania edytowanego laboratorium
     */
    fun showEditDialog(inflater: LayoutInflater, laboratory: Laboratory, tasks: LaboratoryTaskList) {
        dialogLayout = inflater.inflate(R.layout.dialog_laboratory, null)

        mainLaboratory.laboratory = laboratory
        mainLaboratory.tasks = tasks.clone() as LaboratoryTaskList

        setTaskAdapter()
        fillInfo()
        setButtonsOnClicks()

        setTitle(R.string.EditExercise)
        setView(dialogLayout)
        setPositiveButton(R.string.Save) { _, _ ->
            fillLaboratory()
            onSaveClick?.invoke(mainLaboratory)
        }
        val dialog = create()
        dialog.window?.decorView?.clearFocus()
        dialog.show()
    }

    /**
     * Przypisanie adaptera do RecyclerView dialogu
     */
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
                2, mainLaboratory.laboratory.id))
            if (taskAdapter.itemCount > 1)
                taskAdapter.refreshItem(taskAdapter.itemCount - 2)
            hideKeyboard()
            dialogLayout.clearFocus()
        }
    }

    /**
     * Ukrycie klawiatury
     */
    private fun hideKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(dialogLayout.windowToken, 0)
    }

    /**
     * Wyświetlenie dialgu z dodaniem laboratorium
     * @param inflater LayoutInflater aktywności wywołującej
     */
    fun showAddDialog(inflater: LayoutInflater) {
        dialogLayout = inflater.inflate(R.layout.dialog_laboratory, null)

        mainLaboratory.laboratory = Laboratory("","", 0, "", 0,
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
        }
        val dialog = create()
        dialog.window?.decorView?.clearFocus()
        dialog.show()
    }
}
