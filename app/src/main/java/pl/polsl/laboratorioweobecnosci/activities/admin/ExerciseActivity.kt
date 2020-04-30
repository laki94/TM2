package pl.polsl.laboratorioweobecnosci.activities.admin

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import pl.polsl.laboratorioweobecnosci.R
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class ExerciseActivity(context: Context) : AlertDialog.Builder(context) {

    private lateinit var mDateFrom: LocalDateTime
    private lateinit var mDateTo: LocalDateTime
    private lateinit var mButtonStartDate: Button
    private lateinit var mButtonEndDate: Button


    private fun setButtonsText() {
        mButtonStartDate.text =
            context.getString(R.string.BeginExerciseDate,
                mDateFrom.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")))

        mButtonEndDate.text =
            context.getString(R.string.EndExerciseDate,
                mDateTo.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")))
    }

    private fun setButtonsOnClicks() {
        mButtonStartDate.setOnClickListener {
            val newYear = mDateFrom.year
            val newMonth = mDateFrom.month.value
            val newDay = mDateFrom.dayOfMonth
            val newHour = mDateFrom.hour
            val newMinute = mDateFrom.minute

            val timePickerDialog = TimePickerDialog(context,
                TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
                    val newTime = LocalTime.of(hourOfDay, minute)
                    mDateFrom = LocalDateTime.of(mDateFrom.toLocalDate(), newTime)
                    setButtonsText()
                }, newHour, newMinute, true)

            val datePickerDialog = DatePickerDialog(context,
                DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                    val newDate = LocalDate.of(year, month, dayOfMonth)
                    mDateFrom = LocalDateTime.of(newDate, mDateFrom.toLocalTime())
                    setButtonsText()
                    timePickerDialog.show()
                }, newYear, newMonth, newDay)
            datePickerDialog.show()
        }

        mButtonEndDate.setOnClickListener {
            val newYear = mDateTo.year
            val newMonth = mDateTo.month.value
            val newDay = mDateTo.dayOfMonth
            val newHour = mDateTo.hour
            val newMinute = mDateTo.minute

            val timePickerDialog = TimePickerDialog(context,
                TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
                    val newTime = LocalTime.of(hourOfDay, minute)
                    mDateTo = LocalDateTime.of(mDateTo.toLocalDate(), newTime)
                    setButtonsText()
                }, newHour, newMinute, true)

            val datePickerDialog = DatePickerDialog(context,
                DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                    val newDate = LocalDate.of(year, month, dayOfMonth)
                    mDateTo = LocalDateTime.of(newDate, mDateTo.toLocalTime())
                    setButtonsText()
                    timePickerDialog.show()
                }, newYear, newMonth, newDay)
            datePickerDialog.show()
        }
    }

    private fun initializeButtons(dialog: View) {
        setButtonsText()
        setButtonsOnClicks()
    }

    fun showAddDialog(inflater: LayoutInflater) {
        val dialogLayout = inflater.inflate(R.layout.dialog_exercise, null)
        mButtonStartDate = dialogLayout.findViewById(R.id.bExerciseStartDate)
        mButtonEndDate = dialogLayout.findViewById(R.id.bExerciseEndDate)
        mDateFrom = LocalDateTime.now()
        mDateTo = mDateFrom.plusHours(1).plusMinutes(30)

        setTitle(R.string.AddExercise)
        setView(dialogLayout)
        initializeButtons(dialogLayout)
        setPositiveButton(R.string.Save) { _, _ -> }

        super.show()
    }
}
