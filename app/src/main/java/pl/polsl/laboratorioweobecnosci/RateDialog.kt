package pl.polsl.laboratorioweobecnosci

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import pl.polsl.laboratorioweobecnosci.activities.adapters.LaboratoriesAdapter
import pl.polsl.laboratorioweobecnosci.activities.adapters.TasksAdapter
import pl.polsl.laboratorioweobecnosci.activities.student.StudentsListActivity
import pl.polsl.laboratorioweobecnosci.database.DatabaseHandler
import pl.polsl.laboratorioweobecnosci.database.models.LaboratoryTask
import pl.polsl.laboratorioweobecnosci.database.models.StudentListWorkstationModel
import pl.polsl.laboratorioweobecnosci.database.models.WorkstationLaboratoryTask
import pl.polsl.laboratorioweobecnosci.database.models.lists.LaboratoryTaskList

class RateDialog(context: Context) : AlertDialog.Builder(context) {

    private lateinit var dialogLayout: View
    private lateinit var mainTasks: LaboratoryTaskList
    private lateinit var mainWorkstationWithStudents: StudentListWorkstationModel
    private lateinit var mainTasksDone: LaboratoryTaskList
    private lateinit var adapter: TasksAdapter
    private lateinit var spinnerGrade: Spinner


    private fun setTaskAdapter() {
        adapter = TasksAdapter(context, mainTasks, mainTasksDone)

        adapter.let {
            it.onTaskClick = { task, add ->
                if (add) {
                    mainTasksDone.addIfNotExist(task)
                } else {
                    mainTasksDone.remove(task)
                }
                spinnerGrade.setSelection(mainTasksDone.getHighestGrade() - 2)
            }
        }

        val rvTasks = dialogLayout.findViewById<RecyclerView>(R.id.rvTasks)
        rvTasks.layoutManager = LinearLayoutManager(context)
        rvTasks.adapter = adapter
    }

    fun rate(layoutInflater: LayoutInflater, workstationWithStudents: StudentListWorkstationModel,
             tasksToDo: LaboratoryTaskList, tasksDone: LaboratoryTaskList) {
        dialogLayout = layoutInflater.inflate(R.layout.dialog_rate_workstation, null)

        mainTasks = tasksToDo
        mainTasksDone = tasksDone
        mainWorkstationWithStudents = workstationWithStudents
        spinnerGrade = dialogLayout.findViewById(R.id.sGrade)
        spinnerGrade.adapter = ArrayAdapter(context, R.layout.support_simple_spinner_dropdown_item,
            context.resources.getStringArray(R.array.Grades))
        setTaskAdapter()
        setTitle(String.format("%s - %s", layoutInflater.context.getString(R.string.RateWorkstation,
            mainWorkstationWithStudents.workstation.number),
            mainWorkstationWithStudents.students.toStringOnlyLastName()))
        setView(dialogLayout)
        setPositiveButton(R.string.Save) { _, _ -> }
        val dialog = create()
        dialog.show()

        val btn = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
        btn.setOnClickListener {
            Thread {
                val db = DatabaseHandler(context)
                val tmpTasksInDb = db.workstationLaboratoryTaskDao()
                    .getTasksDoneForWorkstationAtLaboratory(mainWorkstationWithStudents.workstation.id,
                    mainWorkstationWithStudents.students[0].laboratoryId)

                val tasksInDb = LaboratoryTaskList()
                tmpTasksInDb.forEach {
                    val task = db.laboratoryTaskDao().getTaskWithId(it.laboratoryTaskId)
                    tasksInDb.add(task)
                    if (!mainTasksDone.haveTask(it)) {
                        db.workstationLaboratoryTaskDao().delete(it)
                    }
                }

                mainTasksDone.iterator().forEach {
                    if (!tasksInDb.haveTask(it)) {
                        val tmpTask = WorkstationLaboratoryTask(it.id,
                            mainWorkstationWithStudents.workstation.id, it.laboratoryId)
                        db.workstationLaboratoryTaskDao().insert(tmpTask)
                    }
                }
            }.start()
            dialog.dismiss()
        }
    }
}
