package pl.polsl.laboratorioweobecnosci.activities.main

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricConstants
import androidx.biometric.BiometricPrompt
import pl.polsl.laboratorioweobecnosci.R
import pl.polsl.laboratorioweobecnosci.activities.admin.AdminActivity
import pl.polsl.laboratorioweobecnosci.activities.admin.LaboratoriesDialog
import pl.polsl.laboratorioweobecnosci.database.DatabaseHandler
import pl.polsl.laboratorioweobecnosci.preferences.AuthorizationMode
import pl.polsl.laboratorioweobecnosci.preferences.PreferencesManager
import pl.polsl.laboratorioweobecnosci.security.AuthorizationManager
import pl.polsl.laboratorioweobecnosci.security.FingerprintAuth

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        FingerprintAuth.instance = FingerprintAuth(this)
        PreferencesManager.instance = PreferencesManager(this)
    }

    private fun openAdminActivity() {
        val intent = Intent(this, AdminActivity::class.java)
        startActivity(intent)
    }

    fun onAdminPanelClick(view: View) {
        AuthorizationManager.instance.doAuthorize(this) { openAdminActivity() }
    }

    fun onBeginExerciseClick(view: View) {
        val dialog = LaboratoriesDialog(this)
        Thread {
            val db = DatabaseHandler(this)
            val laboratories = db.getLaboratoriesSortedByStartDate()
            runOnUiThread {
                dialog.showLaboratoriesForStudents(layoutInflater, laboratories)
            }
        }.start()
    }
}
