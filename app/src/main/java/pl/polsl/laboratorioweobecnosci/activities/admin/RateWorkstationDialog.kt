package pl.polsl.laboratorioweobecnosci.activities.admin

import android.content.Context
import android.view.LayoutInflater
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import pl.polsl.laboratorioweobecnosci.R
import pl.polsl.laboratorioweobecnosci.database.models.Workstation

class RateWorkstationDialog(context: Context) : AlertDialog.Builder(context) {

    fun rate(inflater: LayoutInflater, workstation: Workstation) {
        val dialogLayout = inflater.inflate(R.layout.dialog_rate_workstation, null)
        val spinner = dialogLayout.findViewById<Spinner>(R.id.sGrade)

        spinner.adapter = ArrayAdapter(context, R.layout.support_simple_spinner_dropdown_item,
            context.resources.getStringArray(R.array.Grades))

        val students = dialogLayout.findViewById<TextView>(R.id.tvStudentsInfo)
        students.text = workstation.students().toString()

        setTitle(context.getString(R.string.RateWorkstation, workstation.stationNr()))
        setView(dialogLayout)
        setPositiveButton(R.string.AcceptGrade) { _, _ -> }

        super.show()
    }
}
