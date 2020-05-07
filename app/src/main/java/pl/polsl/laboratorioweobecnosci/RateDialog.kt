package pl.polsl.laboratorioweobecnosci

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import pl.polsl.laboratorioweobecnosci.activities.adapters.LaboratoriesAdapter
import pl.polsl.laboratorioweobecnosci.activities.adapters.TasksAdapter
import pl.polsl.laboratorioweobecnosci.activities.student.StudentsListActivity
import pl.polsl.laboratorioweobecnosci.database.models.LaboratoryTask
import pl.polsl.laboratorioweobecnosci.database.models.StudentListWorkstationModel
import pl.polsl.laboratorioweobecnosci.database.models.lists.LaboratoryTaskList

class RateDialog(context: Context) : AlertDialog.Builder(context) {

    private lateinit var dialogLayout: View
    private lateinit var mainTasks: LaboratoryTaskList
    private lateinit var adapter: TasksAdapter


    private fun setTaskAdapter() {
        adapter = TasksAdapter(context, mainTasks)

        adapter.let {
//            it.onLaboratoryClick = {singleLab ->
//                selectedLaboratory = singleLab
//                it.selectItem(selectedLaboratory.id)
//            }
        }

        val rvTasks = dialogLayout.findViewById<RecyclerView>(R.id.rvTasks)
        rvTasks.layoutManager = LinearLayoutManager(context)
        rvTasks.adapter = adapter
    }

    fun rate(layoutInflater: LayoutInflater, workstationWithStudents: StudentListWorkstationModel, tasks: LaboratoryTaskList) {
        dialogLayout = layoutInflater.inflate(R.layout.dialog_rate_workstation, null)

        mainTasks = tasks
        setTaskAdapter()
        setTitle(R.string.SelectLaboratory)
        setView(dialogLayout)
        setPositiveButton(R.string.Select) { _, _ -> }
        val dialog = create()
        dialog.show()

        val btn = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
        btn.setOnClickListener {
//            if (!this::selectedLaboratory.isInitialized)
//                Toast.makeText(context, R.string.SelectLaboratory, Toast.LENGTH_SHORT).show()
//            else {
//                dialog.dismiss()
//                val intent = Intent(context, StudentsListActivity::class.java)
//                intent.putExtra("LABID", selectedLaboratory.id)
//                ContextCompat.startActivity(context, intent, null)
//            }
        }
    }
}
