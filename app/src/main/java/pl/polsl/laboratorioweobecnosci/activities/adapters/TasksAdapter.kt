package pl.polsl.laboratorioweobecnosci.activities.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.CheckedTextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.button_item.view.*
import kotlinx.android.synthetic.main.rate_task_item.view.*
import pl.polsl.laboratorioweobecnosci.R
import pl.polsl.laboratorioweobecnosci.database.models.LaboratoryTask
import pl.polsl.laboratorioweobecnosci.database.models.Student
import pl.polsl.laboratorioweobecnosci.database.models.Workstation
import pl.polsl.laboratorioweobecnosci.database.models.lists.LaboratoryTaskList
import pl.polsl.laboratorioweobecnosci.database.models.lists.ListOfWorkstationsWithStudents

/**
 * Adapter dla aktywności wyświetlających zadania do wykonania przez studentów
 * @param context context aktywności
 * @param tasksToDo zadania do wykonania przez studentów na stanowisku
 * @param tasksDone zadania wykonane przez studentów na stanowisku
 * @property onTaskClick callback, który zostaje wywołany po wybraniu zadania
 */
class TasksAdapter(private val context: Context, private val tasksToDo: LaboratoryTaskList, private val tasksDone: LaboratoryTaskList): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var onTaskClick: ((LaboratoryTask, Boolean) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MyViewHolder(LayoutInflater.from(context).inflate(R.layout.rate_task_item, parent, false))
    }

    override fun getItemCount(): Int {
        return tasksToDo.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val myHolder = holder as MyViewHolder

        myHolder.cbTask.text = context.getString(R.string.TaskNr, tasksToDo[position].taskNumber)
        myHolder.cbTask.isChecked = tasksDone.haveTask(tasksToDo[position])
    }

    /**
     * @property cbTask element interfejsu wyświetlający zadanie
     */
    inner class MyViewHolder (view: View) : RecyclerView.ViewHolder(view) {
        val cbTask: CheckBox = view.cbGrade

        init {
            cbTask.setOnCheckedChangeListener { _, checked ->
                onTaskClick?.invoke(tasksToDo[adapterPosition], checked)
            }
        }
    }
}