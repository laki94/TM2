package pl.polsl.laboratorioweobecnosci.activities.main

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricConstants
import androidx.biometric.BiometricPrompt
import pl.polsl.laboratorioweobecnosci.R
import pl.polsl.laboratorioweobecnosci.activities.admin.AdminActivity
import pl.polsl.laboratorioweobecnosci.activities.admin.LaboratoriesDialog
import pl.polsl.laboratorioweobecnosci.activities.student.StudentsListActivity
import pl.polsl.laboratorioweobecnosci.database.DatabaseHandler
import pl.polsl.laboratorioweobecnosci.database.models.lists.LaboratoryList
import pl.polsl.laboratorioweobecnosci.preferences.AuthorizationMode
import pl.polsl.laboratorioweobecnosci.preferences.PreferencesManager
import pl.polsl.laboratorioweobecnosci.security.FingerprintAuth

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        FingerprintAuth.instance = FingerprintAuth(this)
        PreferencesManager.instance = PreferencesManager(this)
    }

    private fun runAdminActivityIfAuthorized() {
        if (FingerprintAuth.instance.isAvailable()) {
            val callback = object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    if ((errorCode != BiometricConstants.ERROR_CANCELED) && (errorCode != BiometricConstants.ERROR_USER_CANCELED)) {
                        Toast.makeText(this@MainActivity, errString, Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                }

                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    Toast.makeText(this@MainActivity, R.string.Authorized, Toast.LENGTH_SHORT)
                        .show()
                    openAdminActivity()
                }
            }
            FingerprintAuth.instance.authorize(this, callback)
        } else {
            openAdminActivity()
        }
    }

    private fun openAdminActivity() {
        val intent = Intent(this, AdminActivity::class.java)
        startActivity(intent)
    }

    fun onAdminPanelClick(view: View) {
        if (PreferencesManager.instance.isAuthorizationNeeded()) {
            runAdminActivityIfAuthorized()
        } else {
            openAdminActivity()
        }
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
