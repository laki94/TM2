package pl.polsl.laboratorioweobecnosci.activities.main

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import pl.polsl.laboratorioweobecnosci.R
import pl.polsl.laboratorioweobecnosci.activities.admin.AdminActivity
import pl.polsl.laboratorioweobecnosci.activities.admin.LaboratoriesDialog
import pl.polsl.laboratorioweobecnosci.activities.student.StudentsListActivity
import pl.polsl.laboratorioweobecnosci.database.DatabaseHandler
import pl.polsl.laboratorioweobecnosci.database.models.lists.LaboratoryList

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun onAdminPanelClick(view: View) {
        val intent = Intent(this, AdminActivity::class.java)
        startActivity(intent)
    }

    fun onBeginExerciseClick(view: View) {
        val dialog = LaboratoriesDialog(this)
        Thread {
            val db = DatabaseHandler(this)
            val laboratories = db.getAllLaboratories()
            runOnUiThread {
                dialog.showLaboratoriesForStudents(layoutInflater, laboratories)
            }
        }.start()
    }
}
