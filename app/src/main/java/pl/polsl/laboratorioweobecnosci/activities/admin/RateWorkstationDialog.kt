package pl.polsl.laboratorioweobecnosci.activities.admin

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.AlertDialog
import pl.polsl.laboratorioweobecnosci.R

class RateWorkstationDialog(context: Context) : AlertDialog.Builder(context) {

    fun rate(inflater: LayoutInflater) {
        val dialogLayout = inflater.inflate(R.layout.dialog_rate_workstation, null)
        val spinner = dialogLayout.findViewById<Spinner>(R.id.sGrade)

        spinner.adapter = ArrayAdapter(context, R.layout.support_simple_spinner_dropdown_item,
            context.resources.getStringArray(R.array.Grades))

        setTitle(R.string.RateWorkstation)
        setView(dialogLayout)
        setPositiveButton(R.string.AcceptGrade) { _, _ -> }
        super.show()
    }
}
