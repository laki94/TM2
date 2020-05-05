package pl.polsl.laboratorioweobecnosci.activities.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.card_item.view.*
import pl.polsl.laboratorioweobecnosci.R
import pl.polsl.laboratorioweobecnosci.database.models.Laboratory
import pl.polsl.laboratorioweobecnosci.database.models.Student
import pl.polsl.laboratorioweobecnosci.database.models.lists.LaboratoryList
import pl.polsl.laboratorioweobecnosci.database.models.lists.StudentList

class StudentsAdapter(private val context: Context, private val items: StudentList): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var onStudentClick: ((Student) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MyViewHolder(LayoutInflater.from(context).inflate(R.layout.card_item, parent, false))
    }

    fun removeItem(position: Int) {
        items.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, itemCount)
    }

    fun addNewItem(student: Student) {
        items.add(student)
        notifyItemChanged(itemCount - 1)
    }

    fun editItem(student: Student) {
        notifyItemChanged(items.indexOf(student))
    }

    fun refreshItem(position: Int) {
        notifyItemChanged(position)
    }

    fun restoreItem(student: Student, position: Int) {
        items.add(position, student)
        notifyItemInserted(position)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val myHolder = holder as MyViewHolder

        myHolder.tvItem.text = items[position].toShortString()
    }

    inner class MyViewHolder (view: View) : RecyclerView.ViewHolder(view) {
        val tvItem: TextView = view.tvItem

        init {
            tvItem.setOnClickListener {
                onStudentClick?.invoke(items[adapterPosition])
            }
        }
    }
}