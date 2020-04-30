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

class RateListAdapter(private val context: Context): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MyViewHolder(LayoutInflater.from(context).inflate(R.layout.card_item, parent, false))
    }

    override fun getItemCount(): Int {
        return 0
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val myHolder = holder as MyViewHolder

    }

    inner class MyViewHolder (view: View) : RecyclerView.ViewHolder(view) {
        val bItem: Button = view.bButtonItem
    }
}