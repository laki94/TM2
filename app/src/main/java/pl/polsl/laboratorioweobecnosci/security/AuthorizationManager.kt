package pl.polsl.laboratorioweobecnosci.security

import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricConstants
import androidx.biometric.BiometricPrompt
import pl.polsl.laboratorioweobecnosci.R
import pl.polsl.laboratorioweobecnosci.activities.admin.PasswordDialog
import pl.polsl.laboratorioweobecnosci.preferences.AuthorizationMode
import pl.polsl.laboratorioweobecnosci.preferences.PreferencesManager

/**
 * Menadżer autoryzacji
 * @property onAuthorized callback, wywołany po zautoryzowaniu użytkownika
 * @property activityCalling aktywność, która wywołała menadżera
 * @property instance zmienna przechowująca globalny obiekt menadżera
 * @property LOCK blokada zmiennej globalnej
 */
class AuthorizationManager {

    private lateinit var onAuthorized: () -> Unit
    private lateinit var activityCalling: AppCompatActivity

    /**
     * Funkcja autoryzująca użytkownika
     * @param activity aktywność wywołująca funkcję
     * @param doOnAuthorized callback, który zostanie wywołany po zautoryzowaniu użytkownika
     */
    fun doAuthorize(activity: AppCompatActivity, doOnAuthorized: (() -> Unit)) {
        onAuthorized = doOnAuthorized
        activityCalling = activity
        when (PreferencesManager.getInstance(activityCalling).chosenAuthorizationMethod()) {
            AuthorizationMode.NONE -> onAuthorized()
            AuthorizationMode.PASSWORD -> authorizeWithPassword()
            AuthorizationMode.PIN -> authorizeWithPin()
            AuthorizationMode.FINGERPRINT -> authorizeWithFingerprint()
        }
    }

    /**
     * Wyświetlenie zapytania o hasło do autoryzacji
     */
    private fun authorizeWithPassword() {
        val passwordDialog = PasswordDialog(activityCalling)
        passwordDialog.askForPassword(activityCalling.layoutInflater) { onAuthorized() }
    }

    /**
     * Wyświetlenie zapytania o pin do autoryzacji
     */
    private fun authorizeWithPin() {
        val passwordDialog = PasswordDialog(activityCalling)
        passwordDialog.askForPin(activityCalling.layoutInflater) { onAuthorized() }
    }

    /**
     * Wyświetlenie zapytania o zautoryzowanie palcem
     */
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

            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                Toast.makeText(activityCalling, R.string.Authorized, Toast.LENGTH_SHORT).show()
                onAuthorized()
            }
        }
        FingerprintAuth.getInstance(activityCalling).authorize(activityCalling, callback)
    }

    companion object {
        @Volatile private var instance: AuthorizationManager? = null
        private val LOCK = Any()

        /**
         * Funkcja zwracająca i generująca obiekt menadżera
         * @return menadżer autoryzacji
         */
        fun getInstance() = instance ?: synchronized(LOCK){
            instance ?: AuthorizationManager().also { instance = it}
        }
    }
}