package pl.polsl.laboratorioweobecnosci.activities.main

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricConstants
import androidx.biometric.BiometricPrompt
import pl.polsl.laboratorioweobecnosci.R
import pl.polsl.laboratorioweobecnosci.activities.BaseActivity
import pl.polsl.laboratorioweobecnosci.activities.admin.AdminActivity
import pl.polsl.laboratorioweobecnosci.activities.admin.LaboratoriesDialog
import pl.polsl.laboratorioweobecnosci.database.DatabaseHandler
import pl.polsl.laboratorioweobecnosci.preferences.AuthorizationMode
import pl.polsl.laboratorioweobecnosci.preferences.PreferencesManager
import pl.polsl.laboratorioweobecnosci.security.AuthorizationManager
import pl.polsl.laboratorioweobecnosci.security.FingerprintAuth

/**
 * Główna aktywność aplikacji
 */
class MainActivity : BaseActivity() {

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
        AuthorizationManager.instance.doAuthorize(this) { openAdminActivity() }
    }

    /**
     * Funkcja wywołana po wciśnięciu przycisku rozpoczęcia zajęć. Otwiera dialog z wyborem
     * laboratorium.
     */
    fun onBeginExerciseClick(view: View) {
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
