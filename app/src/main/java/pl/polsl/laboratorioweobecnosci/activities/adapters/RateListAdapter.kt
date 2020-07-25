package pl.polsl.laboratorioweobecnosci.activities.adapters

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.button_item.view.*
import pl.polsl.laboratorioweobecnosci.R
import pl.polsl.laboratorioweobecnosci.database.models.*
import pl.polsl.laboratorioweobecnosci.database.models.lists.WorkstationWithLabDetailsList

/**
 * Adapter dla aktywności wyświetlających ocenianie stanowisk
 * @param context context aktywności
 * @param items lista stanowisk ze studentami, zadaniami wykonanymi, do wykonania i przypisanej ocenie
 * @property onWorkstationClick callback, który zostanie wywołany po wciśnięciu przycisku określającego dane stanowisko
 */
class RateListAdapter(private val context: Context, private val items: WorkstationWithLabDetailsList): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var onWorkstationClick: ((WorkstationWithLabDetails) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MyViewHolder(LayoutInflater.from(context).inflate(R.layout.button_item, parent, false))
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val myHolder = holder as MyViewHolder

        val workstation = items[position].workstationWithStudents.workstation
        val students = items[position].workstationWithStudents.students
        myHolder.bItem.text = String.format("%s\n%s",
            context.getString(R.string.WorkstationNr, workstation.number),
            students.toNewLineSeparatedString())

        when {
            items[position].forcedGrade != null -> myHolder.bItem.setBackgroundColor(Color.YELLOW)
            items[position].haveAllTasksDone() -> myHolder.bItem.setBackgroundColor(Color.GREEN)
            else -> myHolder.bItem.setBackgroundColor(Color.LTGRAY)
        }
    }

    /**
     * @property bItem element interfejsu wyświetlający informacje o stanowisku i studentach
     */
    inner class MyViewHolder (view: View) : RecyclerView.ViewHolder(view) {
        val bItem: Button = view.bButtonItem

        init {
            bItem.setOnClickListener {
                onWorkstationClick?.invoke(items[adapterPosition])
            }
        }
    }
}