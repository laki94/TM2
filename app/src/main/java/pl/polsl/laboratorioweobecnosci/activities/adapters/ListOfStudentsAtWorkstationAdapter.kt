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
import kotlinx.android.synthetic.main.laboratory_item.view.*
import kotlinx.android.synthetic.main.students_workstation_item.view.*
import pl.polsl.laboratorioweobecnosci.R
import pl.polsl.laboratorioweobecnosci.database.models.*

class ListOfStudentsAtWorkstationAdapter(private val context: Context, private val items: ListOfStudentsAtWorkstation): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var onStudentRemove: ((StudentWorkstationModel) -> Unit)? = null
    var onStudentEdit: ((StudentWorkstationModel) -> Unit)? = null

    val p = Paint()
    var swipeEnabled = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MyViewHolder(LayoutInflater.from(context).inflate(R.layout.students_workstation_item, parent, false))
    }

    fun enableSwipe() {
        swipeEnabled = true
    }

    fun addNewStudent(studentAtWorkstation: StudentWorkstationModel) {
        items.addStudentWorkstation(studentAtWorkstation.student, studentAtWorkstation.workstation)
        notifyItemChanged(items.sortedWorkstationPosition(studentAtWorkstation.workstation.number))
    }

    fun editStudent(studentAtWorkstation: StudentWorkstationModel) {
        items.editStudent(studentAtWorkstation)
        notifyItemChanged(items.sortedWorkstationPosition(studentAtWorkstation.workstation.number))
    }

    fun removeStudent(studentAtWorkstation: StudentWorkstationModel) {
        items.removeStudent(studentAtWorkstation)
        if (items.getStudentsAtWorkstationNr(studentAtWorkstation.workstation.number).count() > 0) {
            notifyItemChanged(items.sortedWorkstationPosition(studentAtWorkstation.workstation.number))
        } else {
            notifyDataSetChanged()
        }
    }

    fun refreshItem(position: Int) {
        notifyItemChanged(position)
    }

    override fun getItemCount(): Int {
        return items.getWorkstationsCount()
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val myHolder = holder as MyViewHolder

        val studentsLayoutManager = LinearLayoutManager(myHolder.rvStudents.context, RecyclerView.VERTICAL, false)
        val data = StudentListWorkstationModel()
        data.workstation = items.getSortedWorkstation()[position]
        data.students = items.getStudentsAtWorkstationNr(data.workstation.number)

        myHolder.tvWorkstation.text = data.workstation.toString(context)
            myHolder.rvStudents.apply {
                layoutManager = studentsLayoutManager
                adapter = StudentsAdapter(context, data)
            }
    }

    inner class MyViewHolder (view: View) : RecyclerView.ViewHolder(view) {
        val tvWorkstation: TextView = view.tvWorkstation
        val rvStudents: RecyclerView = view.rvStudents

        init {
            if (swipeEnabled)
                enableSwipe()
        }

        private fun enableSwipe() {
            val simpleItemTouchCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {
                    return false
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    val pos = viewHolder.adapterPosition
                    val data = StudentWorkstationModel()
                    data.workstation = items.getSortedWorkstation()[adapterPosition]
                    data.student = items.getStudentsAtWorkstationNr(data.workstation.number)[pos]

                    if (direction == ItemTouchHelper.LEFT) {
                        removeStudent(data)
                        onStudentRemove?.invoke(data)
                    } else if (direction == ItemTouchHelper.RIGHT) {
                        onStudentEdit?.invoke(data)
                        refreshItem(adapterPosition)
                    }
                }

                override fun onChildDraw(
                    c: Canvas,
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    dX: Float,
                    dY: Float,
                    actionState: Int,
                    isCurrentlyActive: Boolean
                ) {
                    val icon: Bitmap
                    if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
                        val itemView = viewHolder.itemView
                        val height = itemView.bottom.toFloat() - itemView.top.toFloat()
                        val width = height / 3

                        if (dX > 0) {
                            p.color = Color.parseColor("#63f542")
                            val background = RectF(itemView.left.toFloat(), itemView.top.toFloat(), dX, itemView.bottom.toFloat())
                            c.drawRect(background, p)
                            icon = BitmapFactory.decodeResource(context.resources, R.drawable.edit)
                            val icon_dest = RectF(itemView.left.toFloat() + width, itemView.top.toFloat() + width, itemView.left.toFloat() + 2*width, itemView.bottom.toFloat() - width)
                            c.drawBitmap(icon, null, icon_dest, p)
                        } else {
                            p.color = Color.parseColor("#D32F2F")
                            val background = RectF(
                                itemView.right.toFloat() + dX,
                                itemView.top.toFloat(),
                                itemView.right.toFloat(),
                                itemView.bottom.toFloat()
                            )
                            c.drawRect(background, p)
                            icon = BitmapFactory.decodeResource(context.resources, R.drawable.delete)
                            val icon_dest = RectF(
                                itemView.right.toFloat() - 2 * width,
                                itemView.top.toFloat() + width,
                                itemView.right.toFloat() - width,
                                itemView.bottom.toFloat() - width
                            )
                            c.drawBitmap(icon, null, icon_dest, p)
                        }
                    }

                    super.onChildDraw(
                        c,
                        recyclerView,
                        viewHolder,
                        dX,
                        dY,
                        actionState,
                        isCurrentlyActive
                    )
                }
            }
            val itemTouchHelper = ItemTouchHelper(simpleItemTouchCallback)
            itemTouchHelper.attachToRecyclerView(rvStudents)
        }
    }
}