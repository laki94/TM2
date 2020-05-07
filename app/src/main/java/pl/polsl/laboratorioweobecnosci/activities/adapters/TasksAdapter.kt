package pl.polsl.laboratorioweobecnosci.activities.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckedTextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.button_item.view.*
import kotlinx.android.synthetic.main.rate_task_item.view.*
import pl.polsl.laboratorioweobecnosci.R
import pl.polsl.laboratorioweobecnosci.database.models.Student
import pl.polsl.laboratorioweobecnosci.database.models.Workstation
import pl.polsl.laboratorioweobecnosci.database.models.lists.LaboratoryTaskList
import pl.polsl.laboratorioweobecnosci.database.models.lists.ListOfWorkstationsWithStudents

class TasksAdapter(private val context: Context, private val items: LaboratoryTaskList): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MyViewHolder(LayoutInflater.from(context).inflate(R.layout.rate_task_item, parent, false))
    }

    override fun getItemCount(): Int {
        return items.size
    }

//    fun addStudent(student: Student, workstation: Workstation) {
//        items.addStudentToWorkstation(student, workstation)
//        notifyItemInserted(items.sortedWorkstationPosition(workstation.number))
//    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val myHolder = holder as MyViewHolder

        myHolder.ctvTask.text = context.getString(R.string.TaskNr, items[position].taskNumber)
    }

    inner class MyViewHolder (view: View) : RecyclerView.ViewHolder(view) {
        val ctvTask: CheckedTextView = view.ctvTask

        init {
//            bItem.setOnClickListener {
//                onWorkstationClick?.invoke(items[adapterPosition])
//            }
        }
    }
}