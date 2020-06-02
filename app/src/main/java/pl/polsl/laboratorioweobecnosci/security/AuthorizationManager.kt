package pl.polsl.laboratorioweobecnosci.security

import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricConstants
import androidx.biometric.BiometricPrompt
import pl.polsl.laboratorioweobecnosci.R
import pl.polsl.laboratorioweobecnosci.activities.admin.PasswordDialog
import pl.polsl.laboratorioweobecnosci.preferences.AuthorizationMode
import pl.polsl.laboratorioweobecnosci.preferences.PreferencesManager

class AuthorizationManager {

    private lateinit var onAuthorized: () -> Unit
    private lateinit var activityCalling: AppCompatActivity

    fun doAuthorize(activity: AppCompatActivity, doOnAuthorized: (() -> Unit)) {
        onAuthorized = doOnAuthorized
        activityCalling = activity
        when (PreferencesManager.instance.chosenAuthorizationMethod()) {
            AuthorizationMode.NONE -> onAuthorized()
            AuthorizationMode.PASSWORD -> authorizeWithPassword()
            AuthorizationMode.PIN -> authorizeWithPin()
            AuthorizationMode.FINGERPRINT -> authorizeWithFingerprint()
        }
    }

    private fun authorizeWithPassword() {
        val passwordDialog = PasswordDialog(activityCalling)
        passwordDialog.askForPassword(activityCalling.layoutInflater) { onAuthorized() }
    }

    private fun authorizeWithPin() {
        val passwordDialog = PasswordDialog(activityCalling)
        passwordDialog.askForPin(activityCalling.layoutInflater) { onAuthorized() }
    }

    private fun authorizeWithFingerprint() {
        val callback = object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                super.onAuthenticationError(errorCode, errString)
                if ((errorCode != BiometricConstants.ERROR_CANCELED) &&
                    (errorCode != BiometricConstants.ERROR_USER_CANCELED) &&
                    (errorCode != BiometricConstants.ERROR_NEGATIVE_BUTTON)) {
                    Toast.makeText(activityCalling, errString, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()
            }

            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                Toast.makeText(activityCalling, R.string.Authorized, Toast.LENGTH_SHORT).show()
                onAuthorized()
            }
        }
        FingerprintAuth.instance.authorize(activityCalling, callback)
    }

    companion object {
        val instance = AuthorizationManager()
    }
}