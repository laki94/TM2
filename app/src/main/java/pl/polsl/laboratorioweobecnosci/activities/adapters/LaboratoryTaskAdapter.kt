package pl.polsl.laboratorioweobecnosci.activities.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.task_item.view.*
import pl.polsl.laboratorioweobecnosci.R
import pl.polsl.laboratorioweobecnosci.database.models.LaboratoryTask
import pl.polsl.laboratorioweobecnosci.database.models.lists.LaboratoryTaskList

/**
 * Adapter dla aktywności wyświetlających zadania na laboratorium
 * @param context context aktywności
 * @param items lista zadań dla określonego laboratorium
 * @property onRemoveClick Callback, który zostanie wywołany po wciśnięciu przycisku usuwania
 */
class LaboratoryTaskAdapter(private val context: Context, private val items: LaboratoryTaskList): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var onRemoveClick: (() -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MyViewHolder(LayoutInflater.from(context).inflate(R.layout.task_item, parent, false))
    }

    /**
     * Usunięcie zadania
     * @param position pozycja zadania do usunięcia na liście
     */
    fun removeItem(position: Int) {
        items.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, itemCount)
    }

    /**
     * Dodanie nowego zadania
     * @param task zadanie do dodania
     */
    fun addNewItem(task: LaboratoryTask) {
        items.add(task)
        notifyItemInserted(itemCount - 1)
    }

    /**
     * Odświeżenie pojedynczego zadania
     * @param position pozycja zadania na liście
     */
    fun refreshItem(position: Int) {
        notifyItemChanged(position)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val myHolder = holder as MyViewHolder

        myHolder.tvTaskNr.text = items[position].toString(context)
        myHolder.sTaskGrade.setSelection(items[position].degree - 2)

        if (position == itemCount - 1)
            myHolder.ivRemove.visibility = View.VISIBLE
        else
            myHolder.ivRemove.visibility = View.GONE

        myHolder.sTaskGrade.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>, p1: View?, p2: Int, p3: Long) {
                items[position].degree = p0.getItemAtPosition(p2).toString().toInt()
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }
        }
    }

    /**
     * @property tvTaskNr Element interfejsu przechowujący nr zadania
     * @property sTaskGrade Element interfejsu przechowujący ocenę z zadanie
     * @property ivRemove Element interfejsu, po kliknięciu którego zadanie jest usuwane
     */
    inner class MyViewHolder (view: View) : RecyclerView.ViewHolder(view) {

        val tvTaskNr: TextView = view.tvTaskNr
        val sTaskGrade: Spinner = view.sTaskGrade
        val ivRemove: ImageView = view.ivRemove

        init {
            sTaskGrade.adapter = ArrayAdapter(context, R.layout.support_simple_spinner_dropdown_item,
                context.resources.getStringArray(R.array.Grades))

            ivRemove.setOnClickListener {
                onRemoveClick?.invoke()
            }
        }
    }
}