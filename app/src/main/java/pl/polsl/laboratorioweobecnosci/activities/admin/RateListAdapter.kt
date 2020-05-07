package pl.polsl.laboratorioweobecnosci.activities.admin

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.button_item.view.*
import pl.polsl.laboratorioweobecnosci.R
import pl.polsl.laboratorioweobecnosci.database.models.*
import pl.polsl.laboratorioweobecnosci.database.models.lists.ListOfWorkstationsWithStudents
import pl.polsl.laboratorioweobecnosci.database.models.lists.StudentWorkstationLaboratoryList
import kotlin.math.roundToInt

class RateListAdapter(private val context: Context, private val items: ListOfWorkstationsWithStudents): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var onWorkstationClick: ((StudentListWorkstationModel) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MyViewHolder(LayoutInflater.from(context).inflate(R.layout.button_item, parent, false))
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun addStudent(student: Student, workstation: Workstation) {
        items.addStudentToWorkstation(student, workstation)
        notifyItemInserted(items.sortedWorkstationPosition(workstation.number))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val myHolder = holder as MyViewHolder

        myHolder.bItem.text = String.format("%s\n%s",
            context.getString(R.string.WorkstationNr, items[position].workstation.number),
            items[position].students.toNewLineSeparatedString())
    }

    inner class MyViewHolder (view: View) : RecyclerView.ViewHolder(view) {
        val bItem: Button = view.bButtonItem

        init {
            bItem.setOnClickListener {
//                onWorkstationClick?.invoke(items[adapterPosition])
            }
        }
    }
}