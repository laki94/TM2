package pl.polsl.laboratorioweobecnosci.activities.adapters

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.card_item.view.*
import kotlinx.android.synthetic.main.csv_item.view.*
import pl.polsl.laboratorioweobecnosci.R
import pl.polsl.laboratorioweobecnosci.database.models.Laboratory
import pl.polsl.laboratorioweobecnosci.database.models.lists.LaboratoryList

class GenerateCSVAdapter(private val context: Context, private val items: LaboratoryList): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var onGenerateClick: ((Laboratory) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MyViewHolder(LayoutInflater.from(context).inflate(R.layout.csv_item, parent, false))
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val myHolder = holder as MyViewHolder

        myHolder.tvItem.text = items[position].getInfoString(context)
    }

    inner class MyViewHolder (view: View) : RecyclerView.ViewHolder(view) {
        val tvItem: TextView = view.tvCSVItem
        private val bGenerate: Button = view.bGenerateCSVItem
        init {
            bGenerate.setOnClickListener {
                onGenerateClick?.invoke(items[adapterPosition])
            }
        }
    }
}