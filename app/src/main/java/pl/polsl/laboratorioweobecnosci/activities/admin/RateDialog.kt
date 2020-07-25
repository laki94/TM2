package pl.polsl.laboratorioweobecnosci.activities.admin

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import pl.polsl.laboratorioweobecnosci.R
import pl.polsl.laboratorioweobecnosci.activities.adapters.TasksAdapter
import pl.polsl.laboratorioweobecnosci.database.DatabaseHandler
import pl.polsl.laboratorioweobecnosci.database.models.LaboratoryWorkstationGradeModel
import pl.polsl.laboratorioweobecnosci.database.models.WorkstationWithLabDetails
import pl.polsl.laboratorioweobecnosci.database.models.lists.LaboratoryTaskList

/**
 * Dialog wyświetlający ocenianie stanowiska
 * @param context context aktywności wywołującej
 * @property dialogLayout dialog, który zostanie wyświetlony
 * @property adapter adapter to RecyclerView w dialogu
 * @property spinnerGrade Spinner z oceną dla stanowiska
 * @property mainDetails informacje o stanowisku, studentach, zadania do wykonania, wykonanych i przypisanej ocenie
 * @property orgTasksDone oryginalne zadania wykonane przez stanowisko
 * @property orgGrade oryginalna ocena przypisana do stanowiska
 */
class RateDialog(context: Context) : AlertDialog.Builder(context) {

    private lateinit var dialogLayout: View
    private lateinit var adapter: TasksAdapter
    private lateinit var spinnerGrade: Spinner
    private lateinit var mainDetails: WorkstationWithLabDetails
    private val orgTasksDone = LaboratoryTaskList()
    private var orgGrade: LaboratoryWorkstationGradeModel? = null

    /**
     * Ustawienie adaptera klasy
     */
    private fun setTaskAdapter() {
        adapter = TasksAdapter(context, mainDetails.tasksToDo, mainDetails.tasksDone)

        adapter.let {
            it.onTaskClick = { task, add ->
                if (add) {
                    mainDetails.tasksDone.addIfNotExist(task)
                } else {
                    mainDetails.tasksDone.remove(task)
                }
                showProperProposedGrade()
            }
        }

        val rvTasks = dialogLayout.findViewById<RecyclerView>(R.id.rvTasks)
        rvTasks.layoutManager = LinearLayoutManager(context)
        rvTasks.adapter = adapter
    }

    /**
     * Wyświetlenie odpowiedniej oceny na spinnerze
     */
    private fun showProperProposedGrade() {
        when {
            mainDetails.forcedGrade != null -> setGrade(mainDetails.forcedGrade!!.grade)
            else -> setGrade(mainDetails.tasksDone.getHighestGrade())
        }
    }

    /**
     * Wyświetlenie wybranej oceny
     * @param grade wybrana ocena
     */
    private fun setGrade(grade: Int) {
        spinnerGrade.setSelection(grade - 2)
    }

    /**
     * Wyświetlenie dialogu oceniającego stanowisko
     * @param layoutInflater LayoutInflater aktywności wywołującej
     * @param workstationWithLabDetails informacje o stanowisku ze studentami, zadaniami wykonanymi, do wykonania i przypisanej ocenie
     */
    fun rate(layoutInflater: LayoutInflater, workstationWithLabDetails: WorkstationWithLabDetails) {
        dialogLayout = layoutInflater.inflate(R.layout.dialog_rate_workstation, null)

        mainDetails = workstationWithLabDetails
        orgTasksDone.addAll(mainDetails.tasksDone)
        orgGrade = mainDetails.forcedGrade?.clone()

        spinnerGrade = dialogLayout.findViewById(R.id.sGrade)
        spinnerGrade.adapter = ArrayAdapter(context,
            R.layout.support_simple_spinner_dropdown_item,
            context.resources.getStringArray(R.array.Grades))
        spinnerGrade.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                if (mainDetails.tasksDone.getHighestGrade() != spinnerGrade.selectedItem.toString().toInt()) {
                    if (mainDetails.forcedGrade == null) {
                        mainDetails.forcedGrade = LaboratoryWorkstationGradeModel(
                            mainDetails.workstationWithStudents.students[0].laboratoryId,
                            mainDetails.workstationWithStudents.workstation.id
                        )
                    }
                    mainDetails.forcedGrade?.grade = spinnerGrade.selectedItem.toString().toInt()
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }
        }
        setTaskAdapter()
        setTitle(String.format("%s - %s", layoutInflater.context.getString(
            R.string.RateWorkstation,
            mainDetails.workstationWithStudents.workstation.number),
            mainDetails.workstationWithStudents.students.toStringOnlyLastName()))
        setView(dialogLayout)
        setPositiveButton(R.string.Save) { _, _ -> }
        val dialog = create()
        dialog.show()

        showProperProposedGrade()
        val btn = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
        btn.setOnClickListener {
            Thread {
                val db = DatabaseHandler(context)
                db.synchronizeTasksDone(orgTasksDone, mainDetails)
                db.synchronizeForcedGrade(orgGrade, mainDetails.forcedGrade)
            }.start()
            dialog.dismiss()
        }
    }
}
