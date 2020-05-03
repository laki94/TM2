package pl.polsl.laboratorioweobecnosci.activities.admin

import android.graphics.*
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import pl.polsl.laboratorioweobecnosci.R
import pl.polsl.laboratorioweobecnosci.activities.adapters.LaboratoryAdapter
import pl.polsl.laboratorioweobecnosci.database.DatabaseHandler
import pl.polsl.laboratorioweobecnosci.database.models.Laboratory
import pl.polsl.laboratorioweobecnosci.database.models.LaboratoryTask
import pl.polsl.laboratorioweobecnosci.database.models.LaboratoryTaskModel
import pl.polsl.laboratorioweobecnosci.database.models.lists.LaboratoryList
import pl.polsl.laboratorioweobecnosci.database.models.lists.LaboratoryTaskList

class LaboratoriesActivity : AppCompatActivity() {

    private lateinit var adapter: LaboratoryAdapter
    private var laboratories = LaboratoryList()
    private val p = Paint()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_laboratories)

        adapter = LaboratoryAdapter(this, laboratories)

        adapter.let {
            it.onLaboratoryClick = {
                Thread {
                    val db = DatabaseHandler(this)
                    val tasks = db.laboratoryTaskDao().getTasksForClass(it.id.toInt())
                    val arrListTasks = LaboratoryTaskList()
                    tasks.iterator().forEachRemaining {
                        arrListTasks.add(it)
                    }

                    runOnUiThread {
                        editLaboratory(it, arrListTasks)
                    }
                }.start()
            }
        }
        val rvList = findViewById<RecyclerView>(R.id.rvLaboratories)
        rvList.layoutManager = LinearLayoutManager(this)
        rvList.adapter = adapter

        Thread {
            val db = DatabaseHandler(this)
            val list = db.laboratoryDao().getLaboratories()
            list.iterator().forEachRemaining {
                laboratories.add(it)
            }
        }.start()
        enableSwipe()
    }

    private fun editLaboratory(laboratory: Laboratory, tasks: LaboratoryTaskList) {
        val dialog = LaboratoryDialog(this)
        dialog.onSaveClick = { it ->
            Thread {
                val db = DatabaseHandler(this)
                db.laboratoryDao().update(it.laboratory)
                db.laboratoryTaskDao().updateAll(it.tasks)
                runOnUiThread {
                    adapter.editItem(it.laboratory)
                }
            }.start()
        }
        dialog.showEditDialog(layoutInflater, laboratory, tasks)
    }

    private fun enableSwipe() {
        val rvLaboratories = findViewById<RecyclerView>(R.id.rvLaboratories)
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

                if (direction == ItemTouchHelper.LEFT) {
                    val delLaboratory = laboratories[pos]
                    adapter.removeItem(pos)
                    laboratories.remove(delLaboratory)

                    val snackbar = Snackbar.make(findViewById(R.id.clLaboratories),
                        R.string.LaboratoryRemoved, Snackbar.LENGTH_INDEFINITE)
                    snackbar.setAction("UNDO") {
                        adapter.restoreItem(delLaboratory, pos)
                    }
                    snackbar.addCallback(object : Snackbar.Callback() {
                        override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                            if (event != DISMISS_EVENT_ACTION) {
                                Thread {
                                    val db = DatabaseHandler(this@LaboratoriesActivity)
                                    db.laboratoryDao().delete(delLaboratory)
                                    if (delLaboratory.id != 0L)
                                        db.laboratoryTaskDao().deleteTaskWithLabId(delLaboratory.id)
                                }.start()
                            }
                            super.onDismissed(transientBottomBar, event)
                        }
                    })
                    snackbar.setActionTextColor(Color.YELLOW)
                    snackbar.show()
                } else if (direction == ItemTouchHelper.RIGHT) {
                    Thread {
                        val edLaboratory = laboratories[pos]
                        val db = DatabaseHandler(this@LaboratoriesActivity)
                        val tasks = db.laboratoryTaskDao().getTasksForClass(edLaboratory.id.toInt())

                        val arrTasks = LaboratoryTaskList()

                        tasks.iterator().forEachRemaining {
                            arrTasks.add(it)
                        }

                        runOnUiThread {
                            editLaboratory(edLaboratory, arrTasks)
                            adapter.refreshItem(pos)
                        }
                    }.start()
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
                        icon = BitmapFactory.decodeResource(resources, R.drawable.edit)
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
                        icon = BitmapFactory.decodeResource(resources, R.drawable.delete)
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
        itemTouchHelper.attachToRecyclerView(rvLaboratories)
    }

    fun onAddExerciseClick(view: View) {
        val dialog = LaboratoryDialog(this)
        dialog.onSaveClick = { it ->
            Thread {
                val db = DatabaseHandler(this)
                val labId = db.laboratoryDao().insert(it.laboratory)
                it.laboratory.id = labId
                it.tasks.setLaboratoryId(labId.toInt())
                db.laboratoryTaskDao().insertAll(it.tasks)
                runOnUiThread {
                    adapter.addNewItem(it.laboratory)
                }
            }.start()
        }
        dialog.showAddDialog(layoutInflater)
    }
}
