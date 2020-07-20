package pl.polsl.laboratorioweobecnosci.activities.main

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import pl.polsl.laboratorioweobecnosci.R
import pl.polsl.laboratorioweobecnosci.activities.admin.AdminActivity
import pl.polsl.laboratorioweobecnosci.activities.admin.LaboratoriesDialog
import pl.polsl.laboratorioweobecnosci.database.DatabaseHandler
import pl.polsl.laboratorioweobecnosci.security.AuthorizationManager

/**
 * Główna aktywność aplikacji
 */
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    /**
     * Otwiera aktywność z panelem administratora
     */
    private fun openAdminActivity() {
        val intent = Intent(this, AdminActivity::class.java)
        startActivity(intent)
    }

    /**
     * Funkcja wywołana po wciśnięciu przycisku z panelem adminstratora
     */
    fun onAdminPanelClick(view: View) {
        AuthorizationManager.getInstance().doAuthorize(this) { openAdminActivity() }
    }

    /**
     * Funkcja wywołana po wciśnięciu przycisku rozpoczęcia zajęć.
     */
    fun onBeginExerciseClick(view: View) {
        AuthorizationManager.getInstance().doAuthorize(this) { openExerciesDialog() }
    }

    /**
     * Otwarcie dialogu z dostępnymi laboratoriami
     */
    private fun openExerciesDialog() {
        val dialog = LaboratoriesDialog(this)
        Thread {
            val db = DatabaseHandler(this)
            val laboratories = db.getLaboratoriesSortedByStartDate()
            runOnUiThread {
                if (laboratories.isEmpty()) {
                    Toast.makeText(this, R.string.NoLaboratoriesToShow, Toast.LENGTH_LONG).show()
                } else {
                    dialog.showLaboratoriesForStudents(layoutInflater, laboratories)
                }
            }
        }.start()
    }
}
