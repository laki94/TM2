package pl.polsl.laboratorioweobecnosci.activities.adapters

import android.content.Context
import android.graphics.*
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.card_item.view.*
import kotlinx.android.synthetic.main.students_workstation_item.view.*
import pl.polsl.laboratorioweobecnosci.R
import pl.polsl.laboratorioweobecnosci.database.DatabaseHandler
import pl.polsl.laboratorioweobecnosci.database.models.ListOfStudentsAtWorkstation
import pl.polsl.laboratorioweobecnosci.database.models.StudentListWorkstationModel
import pl.polsl.laboratorioweobecnosci.database.models.StudentWorkstationModel

/**
 * Adapter dla aktywności wyświetlających studentów na danym stanowisku
 * @param context context aktywności
 * @param items lista studentów na danym stanowisku
 */
class StudentsAdapter(private val context: Context, private val items: StudentListWorkstationModel): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MyViewHolder(LayoutInflater.from(context).inflate(R.layout.card_item_margin, parent, false))
    }

    override fun getItemCount(): Int {
        return items.students.count()
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val myHolder = holder as MyViewHolder

        myHolder.tvItem.text = String.format(items.students[position].toShortString())
    }

    /**
     * @property tvItem element interfejsu wyświetlający studentów
     */
    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvItem: TextView = view.tvItem
    }
}