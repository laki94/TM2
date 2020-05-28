package pl.polsl.laboratorioweobecnosci.activities.admin

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import pl.polsl.laboratorioweobecnosci.R
import pl.polsl.laboratorioweobecnosci.activities.adapters.TasksAdapter
import pl.polsl.laboratorioweobecnosci.database.DatabaseHandler
import pl.polsl.laboratorioweobecnosci.database.models.LaboratoryWorkstationGradeModel
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
    private lateinit var mainGrade: LaboratoryWorkstationGradeModel


    private fun setTaskAdapter() {
        adapter = TasksAdapter(context, mainTasks, mainTasksDone)

        adapter.let {
            it.onTaskClick = { task, add ->
                if (add) {
                    mainTasksDone.addIfNotExist(task)
                } else {
                    mainTasksDone.remove(task)
                }
                setGrade(mainTasksDone.getHighestGrade())
            }
        }

        val rvTasks = dialogLayout.findViewById<RecyclerView>(R.id.rvTasks)
        rvTasks.layoutManager = LinearLayoutManager(context)
        rvTasks.adapter = adapter
    }

    private fun setGrade(grade: Int) {
        if (grade < mainGrade.grade) {
            spinnerGrade.setSelection(mainGrade.grade - 2)
        } else {
            spinnerGrade.setSelection(grade - 2)
        }
    }

    fun rate(layoutInflater: LayoutInflater, workstationWithStudents: StudentListWorkstationModel,
             tasksToDo: LaboratoryTaskList, tasksDone: LaboratoryTaskList, gradeModel: LaboratoryWorkstationGradeModel) {
        dialogLayout = layoutInflater.inflate(R.layout.dialog_rate_workstation, null)

        mainTasks = tasksToDo
        mainTasksDone = tasksDone
        mainGrade = gradeModel
        spinnerGrade = dialogLayout.findViewById(R.id.sGrade)
        mainWorkstationWithStudents = workstationWithStudents
        spinnerGrade.adapter = ArrayAdapter(context,
            R.layout.support_simple_spinner_dropdown_item,
            context.resources.getStringArray(R.array.Grades))
        setTaskAdapter()
        setTitle(String.format("%s - %s", layoutInflater.context.getString(
            R.string.RateWorkstation,
            mainWorkstationWithStudents.workstation.number),
            mainWorkstationWithStudents.students.toStringOnlyLastName()))
        setView(dialogLayout)
        setPositiveButton(R.string.Save) { _, _ -> }
        val dialog = create()
        dialog.show()

        setGrade(mainGrade.grade)

        val btn = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
        btn.setOnClickListener {
            Thread {
                val db = DatabaseHandler(context)
                val tasksDoneInDB = db.getTasksDoneByWorkstationAtLaboratory(
                    mainWorkstationWithStudents.students[0].laboratoryId,
                    mainWorkstationWithStudents.workstation.id)

                tasksDoneInDB.forEach {
                    if (!mainTasksDone.haveTask(it)) {
                        val task = db.workstationLaboratoryTaskDao()
                            .getTaskForWorkstationAtLaboratoryWithId(
                                mainWorkstationWithStudents.workstation.id, it.laboratoryId, it.id)
                        db.workstationLaboratoryTaskDao().delete(task)
                    }
                }

                mainTasksDone.forEach {
                    if (!tasksDoneInDB.haveTask(it)) {
                        val tmpTask = WorkstationLaboratoryTask(it.id,
                            mainWorkstationWithStudents.workstation.id, it.laboratoryId)
                        db.workstationLaboratoryTaskDao().insert(tmpTask)
                    }
                }
                
                if (mainGrade.id == 0) {
                    val newGrade = LaboratoryWorkstationGradeModel(
                        mainWorkstationWithStudents.students[0].laboratoryId,
                        mainWorkstationWithStudents.workstation.id,
                        spinnerGrade.selectedItem.toString().toInt())
                    mainGrade.id = db.laboratoryGradeDao().insert(newGrade).toInt()
                } else if (mainGrade.grade != spinnerGrade.selectedItem.toString().toInt()) {
                    mainGrade.grade = spinnerGrade.selectedItem.toString().toInt()
                    db.laboratoryGradeDao().update(mainGrade)
                }
            }.start()
            dialog.dismiss()
        }
    }
}
