package pl.polsl.laboratorioweobecnosci.activities.admin

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Telephony
import android.view.LayoutInflater
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.common.base.Strings
import pl.polsl.laboratorioweobecnosci.R
import pl.polsl.laboratorioweobecnosci.activities.adapters.LaboratoriesAdapter
import pl.polsl.laboratorioweobecnosci.activities.adapters.LaboratoryTaskAdapter
import pl.polsl.laboratorioweobecnosci.activities.student.StudentsListActivity
import pl.polsl.laboratorioweobecnosci.database.DatabaseHandler
import pl.polsl.laboratorioweobecnosci.database.models.Laboratory
import pl.polsl.laboratorioweobecnosci.database.models.lists.LaboratoryList

class LaboratoriesDialog(context: Context) : AlertDialog.Builder(context) {

    private lateinit var laboratoriesAdapter: LaboratoriesAdapter
    private lateinit var laboratories: LaboratoryList
    private lateinit var dialogLayout: View
    private lateinit var selectedLaboratory: Laboratory


    private fun setTaskAdapter() {
        laboratoriesAdapter = LaboratoriesAdapter(context, laboratories)

        laboratoriesAdapter.let {
            it.onLaboratoryClick = {singleLab ->
                selectedLaboratory = singleLab
                it.selectItem(selectedLaboratory.id)
            }
        }

        val rvList = dialogLayout.findViewById<RecyclerView>(R.id.rvLaboratories)
        rvList.layoutManager = LinearLayoutManager(context)
        rvList.adapter = laboratoriesAdapter
    }

    fun showLaboratoriesForRating(inflater: LayoutInflater, allLaboratories: LaboratoryList) {
        dialogLayout = inflater.inflate(R.layout.dialog_laboratories, null)
        laboratories = allLaboratories

        setTaskAdapter()
        setTitle(R.string.SelectLaboratory)
        setView(dialogLayout)
        setPositiveButton(R.string.Select) { _, _ -> }
        val dialog = create()
        dialog.show()

        val btn = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
        btn.setOnClickListener {
            if (!this::selectedLaboratory.isInitialized)
                Toast.makeText(context, R.string.SelectLaboratory, Toast.LENGTH_SHORT).show()
            else {
                dialog.dismiss()
                val intent = Intent(context, RateActivity::class.java)
                intent.putExtra("LABID", selectedLaboratory.id)
                startActivity(context, intent, null)
            }
        }
    }

    fun showLaboratoriesForStudents(inflater: LayoutInflater, allLaboratories: LaboratoryList) {
        dialogLayout = inflater.inflate(R.layout.dialog_laboratories, null)
        laboratories = allLaboratories

        setTaskAdapter()
        setTitle(R.string.SelectLaboratory)
        setView(dialogLayout)
        setPositiveButton(R.string.Select) { _, _ -> }
        val dialog = create()
        dialog.show()

        val btn = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
        btn.setOnClickListener {
            if (!this::selectedLaboratory.isInitialized)
                Toast.makeText(context, R.string.SelectLaboratory, Toast.LENGTH_SHORT).show()
            else {
                dialog.dismiss()
                val intent = Intent(context, StudentsListActivity::class.java)
                intent.putExtra("LABID", selectedLaboratory.id)
                startActivity(context, intent, null)
            }
        }
    }
}

