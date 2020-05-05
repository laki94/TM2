package pl.polsl.laboratorioweobecnosci.activities.adapters

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.card_item.view.*
import pl.polsl.laboratorioweobecnosci.R
import pl.polsl.laboratorioweobecnosci.database.models.Laboratory
import pl.polsl.laboratorioweobecnosci.database.models.lists.LaboratoryList

class LaboratoriesAdapter(private val context: Context, private val items: LaboratoryList): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var onLaboratoryClick: ((Laboratory) -> Unit)? = null
    private var selectedLabId = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MyViewHolder(LayoutInflater.from(context).inflate(R.layout.card_item, parent, false))
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun selectItem(labId: Int) {
        selectedLabId = labId
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val myHolder = holder as MyViewHolder

        myHolder.tvItem.text = items[position].getInfoString(context)
        if (items[position].id == selectedLabId)
            myHolder.tvItem.setBackgroundColor(Color.GREEN)
        else
            myHolder.tvItem.setBackgroundColor(Color.WHITE)
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