package pl.polsl.laboratorioweobecnosci.activities.student

import android.graphics.*
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import pl.polsl.laboratorioweobecnosci.R
import pl.polsl.laboratorioweobecnosci.activities.adapters.StudentsAdapter
import pl.polsl.laboratorioweobecnosci.database.DatabaseHandler
import pl.polsl.laboratorioweobecnosci.database.models.Laboratory
import pl.polsl.laboratorioweobecnosci.database.models.Student
import pl.polsl.laboratorioweobecnosci.database.models.lists.LaboratoryTaskList
import pl.polsl.laboratorioweobecnosci.database.models.lists.StudentList

class StudentsListActivity : AppCompatActivity() {

    private lateinit var studentsAdapter: StudentsAdapter
    private var studentsAtLaboratory = StudentList()
    private var laboratoryId = 0
    private val p = Paint()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_students_list)

        studentsAdapter = StudentsAdapter(this, studentsAtLaboratory)

        val rvList = findViewById<RecyclerView>(R.id.rvStudents)
        rvList.layoutManager = LinearLayoutManager(this)
        rvList.adapter = studentsAdapter

        val extras = intent.extras
        if (extras != null) {
            laboratoryId = extras.getInt("LABID", 0)
            if (extras.getBoolean("EDITENABLED", false))
                enableSwipe()
        }

        Thread {
            val db = DatabaseHandler(this)
            val list = db.studentDao().getStudentsOnLaboratory(laboratoryId)

            list.iterator().forEachRemaining {
                studentsAtLaboratory.add(it)
            }

            runOnUiThread {
                studentsAdapter.notifyDataSetChanged()
            }
        }.start()
    }

    fun onAddStudentClick(view: View) {
        val dialog = StudentDialog(this)
        dialog.onSaveClick = {
            Thread {
                val db = DatabaseHandler(this)
                var workstationId = db.workstationDao().getWorkstationId(it.workstation.number)
                if (workstationId == 0)
                    workstationId = db.workstationDao().insert(it.workstation).toInt()
                it.student.workstationId = workstationId
                db.studentDao().insert(it.student)
                runOnUiThread {
                    studentsAdapter.addNewItem(it.student)
                }
            }.start()
        }
        dialog.addStudent(layoutInflater, laboratoryId)
    }

    fun editStudent(student: Student) {
        val dialog = StudentDialog(this)
        dialog.onSaveClick = {
            Thread {
                val db = DatabaseHandler(this)
                var workstationId = db.workstationDao().getWorkstationId(it.workstation.number)
                if (workstationId == 0)
                    workstationId = db.workstationDao().insert(it.workstation).toInt()
                it.student.workstationId = workstationId
                db.studentDao().update(it.student)
                runOnUiThread {
                    studentsAdapter.editItem(student)
                }
            }.start()
        }
        Thread {
            val db = DatabaseHandler(this)
            val workstation = db.workstationDao().getWorkstation(student.workstationId)
            runOnUiThread{
                dialog.editStudent(student, workstation, layoutInflater)
            }
        }.start()
    }

    private fun enableSwipe() {
        val rvStudents = findViewById<RecyclerView>(R.id.rvStudents)
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
                    val delStudent = studentsAtLaboratory[pos]
                    studentsAdapter.removeItem(pos)
                    studentsAtLaboratory.remove(delStudent)

                    val snackbar = Snackbar.make(findViewById(R.id.clStudents),
                        R.string.LaboratoryRemoved, Snackbar.LENGTH_INDEFINITE)
                    snackbar.setAction("UNDO") {
                        studentsAdapter.restoreItem(delStudent, pos)
                    }
                    snackbar.addCallback(object : Snackbar.Callback() {
                        override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                            if (event != DISMISS_EVENT_ACTION) {
                                Thread {
                                    val db = DatabaseHandler(this@StudentsListActivity)
                                    db.studentDao().delete(delStudent)
                                }.start()
                            }
                            super.onDismissed(transientBottomBar, event)
                        }
                    })
                    snackbar.setActionTextColor(Color.YELLOW)
                    snackbar.show()
                } else if (direction == ItemTouchHelper.RIGHT) {
                    val edStudent = studentsAtLaboratory[pos]
                    editStudent(edStudent)
                    studentsAdapter.refreshItem(pos)
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
        itemTouchHelper.attachToRecyclerView(rvStudents)
    }
}
