package pl.polsl.laboratorioweobecnosci.activities.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.button_item.view.*
import kotlinx.android.synthetic.main.card_item.view.*
import pl.polsl.laboratorioweobecnosci.R
import pl.polsl.laboratorioweobecnosci.database.models.Laboratory
import pl.polsl.laboratorioweobecnosci.database.models.LaboratoryTask
import pl.polsl.laboratorioweobecnosci.database.models.LaboratoryTaskModel
import pl.polsl.laboratorioweobecnosci.database.models.StudentWorkstationLaboratory
import pl.polsl.laboratorioweobecnosci.database.models.lists.LaboratoryList

class LaboratoryAdapter(private val context: Context, private val items: LaboratoryList): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var onLaboratoryClick: ((Laboratory) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MyViewHolder(LayoutInflater.from(context).inflate(R.layout.card_item, parent, false))
    }

    fun removeItem(position: Int) {
        items.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, itemCount)
    }

    fun addNewItem(laboratory: Laboratory) {
        items.add(laboratory)
        notifyItemChanged(itemCount - 1)
    }

    fun editItem(laboratory: Laboratory) {
        notifyItemChanged(items.indexOf(laboratory))
    }

    fun refreshItem(position: Int) {
        notifyItemChanged(position)
    }

    fun restoreItem(laboratory: Laboratory, position: Int) {
        items.add(position, laboratory)
        notifyItemInserted(position)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val myHolder = holder as MyViewHolder

        myHolder.tvItem.text = items[position].getInfoString(context)
    }

    inner class MyViewHolder (view: View) : RecyclerView.ViewHolder(view) {
        val tvItem: TextView = view.tvItem

        init {
            tvItem.setOnClickListener {
                onLaboratoryClick?.invoke(items[adapterPosition])
            }
        }
    }
}