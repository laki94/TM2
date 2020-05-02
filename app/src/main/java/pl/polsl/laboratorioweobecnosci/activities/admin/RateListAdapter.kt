package pl.polsl.laboratorioweobecnosci.activities.admin

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.button_item.view.*
import kotlinx.android.synthetic.main.card_item.view.*
import pl.polsl.laboratorioweobecnosci.R
import pl.polsl.laboratorioweobecnosci.models.Workstation
import pl.polsl.laboratorioweobecnosci.models.WorkstationsList

class RateListAdapter(private val context: Context, private val items: WorkstationsList): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var onWorkstationClick: ((Workstation) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MyViewHolder(LayoutInflater.from(context).inflate(R.layout.button_item, parent, false))
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val myHolder = holder as MyViewHolder

        myHolder.bItem.text = items[position].toString(context)
    }

    inner class MyViewHolder (view: View) : RecyclerView.ViewHolder(view) {
        val bItem: Button = view.bButtonItem

        init {
            bItem.setOnClickListener {
                onWorkstationClick?.invoke(items[adapterPosition])
            }
        }
    }
}