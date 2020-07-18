package pl.polsl.laboratorioweobecnosci.activities.adapters

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.card_item.view.*
import pl.polsl.laboratorioweobecnosci.R
import pl.polsl.laboratorioweobecnosci.database.models.Laboratory
import pl.polsl.laboratorioweobecnosci.database.models.lists.LaboratoryList

/**
 * Adapter dla aktywności związanych z wyświetlaniem laboratoriów
 * @param context context aktywności
 * @param items lista laboratoriów które mają zostać wyświetlone
 * @property onLaboratoryClick Callback, który zostanie wywołany po wciśnięciu pojedynczego laboratorium.
 * @property selectedLabId Zmienna przechowująca ID aktualnie wybranego laboratorium
 */
class LaboratoriesAdapter(private val context: Context, private val items: LaboratoryList): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var onLaboratoryClick: ((Laboratory) -> Unit)? = null
    private var selectedLabId = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MyViewHolder(LayoutInflater.from(context).inflate(R.layout.card_item, parent, false))
    }

    override fun getItemCount(): Int {
        return items.size
    }

    /**
     * Zaznacza wybrane laboratorium
     * @param labId Id aktualnie wybranego laboratorium
     */
    fun selectItem(labId: Int) {
        selectedLabId = labId
        notifyDataSetChanged()
    }

    /**
     * Usuwa laboratorium z listy
     * @param position pozycja laboratorium na liście
     */
    fun removeItem(position: Int) {
        items.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, itemCount)
    }

    /**
     * Dodaje laboratorium na listę
     * @param laboratory laboratorium do dodania
     */
    fun addNewItem(laboratory: Laboratory) {
        items.add(laboratory)
        refreshItem(itemCount - 1)
    }

    /**
     * Odświeżenie edytowanego laboratorium
     * @param laboratory laboratorium do odświeżenia
     */
    fun editItem(laboratory: Laboratory) {
        refreshItem(items.indexOf(laboratory))
    }

    /**
     * Odświeża pojedyncze laboratorium
     * @param position pozycja laboratorium do odświeżenia
     */
    fun refreshItem(position: Int) {
        notifyItemChanged(position)
    }

    /**
     * Przywraca laboratorium
     * @param laboratory laboratorium do przywrócenia
     * @param position pozycja na którą laboratorium ma zostać przywrócone
     */
    fun restoreItem(laboratory: Laboratory, position: Int) {
        items.add(position, laboratory)
        notifyItemInserted(position)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val myHolder = holder as MyViewHolder

        myHolder.tvItem.text = items[position].getInfoString(context)
        if (items[position].id == selectedLabId)
            myHolder.cardLayout.setBackgroundColor(Color.YELLOW)
        else
            myHolder.cardLayout.setBackgroundColor(Color.WHITE)
    }

    /**
     * @property tvItem Element interfejsu wyświetlający informacje o laboratorium
     * @property cardLayout Element interfejsu przechowujący wszystkie laboratoria
     */
    inner class MyViewHolder (view: View) : RecyclerView.ViewHolder(view) {

        val tvItem: TextView = view.tvItem
        val cardLayout: CardView = view.cvItem

        init {
            tvItem.setOnClickListener {
                onLaboratoryClick?.invoke(items[adapterPosition])
            }
        }
    }
}