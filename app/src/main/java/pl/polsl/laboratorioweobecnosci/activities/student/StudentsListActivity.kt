package pl.polsl.laboratorioweobecnosci.activities.student

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import pl.polsl.laboratorioweobecnosci.R
import pl.polsl.laboratorioweobecnosci.activities.adapters.StudentsAdapter
import pl.polsl.laboratorioweobecnosci.database.DatabaseHandler
import pl.polsl.laboratorioweobecnosci.database.models.Laboratory
import pl.polsl.laboratorioweobecnosci.database.models.lists.StudentList

class StudentsListActivity : AppCompatActivity() {

    private lateinit var studentsAdapter: StudentsAdapter
    private var studentsAtLaboratory = StudentList()
    private var laboratoryId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_students_list)

        studentsAdapter = StudentsAdapter(this, studentsAtLaboratory)

        val extras = intent.extras
        if (extras != null)
            if (extras.containsKey("LABID")) {
                laboratoryId = extras.getInt("LABID")
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

        val rvList = findViewById<RecyclerView>(R.id.rvStudents)
        rvList.layoutManager = LinearLayoutManager(this)
        rvList.adapter = studentsAdapter
    }

    fun onAddStudentClick(view: View) {
        val dialog = StudentDialog(this)
        dialog.onSaveClick = {
            runOnUiThread {
                studentsAdapter.addNewItem(it)
            }
        }
        dialog.addStudent(layoutInflater, laboratoryId)
    }
}
