package pl.polsl.laboratorioweobecnosci.security

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import pl.polsl.laboratorioweobecnosci.R
import pl.polsl.laboratorioweobecnosci.preferences.PreferencesManager
import java.util.concurrent.Executor

/**
 * Klasa do przetwarzania autoryzacji odciskiem palca
 * @param context context aktywności wywołującej
 * @property biometricManager menadżer biometryczny
 * @property instance zmienna przechowująca globalny obiekt menadżera
 * @property LOCK blokada zmiennej globalnej
 */
class FingerprintAuth(private val context: Context) {
    private val TAG = "FINGERPRINTAUTH"
    private val biometricManager = BiometricManager.from(context)

    /**
     * Sprawdzenie czy autoryzacja odciskiem palca jest możliwa
     * @return True jeśli autoryzacja odciskiem palca jest możliwa
     */
    fun isAvailable(): Boolean {
        when (biometricManager.canAuthenticate()) {
            BiometricManager.BIOMETRIC_SUCCESS -> {
                Log.d(TAG, "App can authenticate using biometrics.")
                return true
            }
            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE ->
                Log.e(TAG, "No biometric features available on this device.")
            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE ->
                Log.e(TAG, "Biometric features are currently unavailable.")
            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED ->
                Log.e(TAG, "The user hasn't associated " +
                        "any biometric credentials with their account.")
        }
        return false
    }

    /**
     * Wywołanie okna z autoryzacją użytkownika poprzez odcisk palca
     * @param activity aktywność wywołująca funkcję
     * @param callbackObject callback wywołany po zautoryzowaniu użytkownika
     */
    fun authorize(activity: AppCompatActivity, callbackObject: BiometricPrompt.AuthenticationCallback) {
        val executor = ContextCompat.getMainExecutor(context)

        val prompt = BiometricPrompt(activity, executor, callbackObject)

        if(PreferencesManager.getInstance(context).optionalAuthorizationEnabled()) {
            prompt.authenticate(
                BiometricPrompt.PromptInfo.Builder()
                    .setTitle(context.getString(R.string.AuthorisationNeeded))
                    .setConfirmationRequired(false)
                    .setDeviceCredentialAllowed(true)
                    .build())
        } else {
            prompt.authenticate(
                BiometricPrompt.PromptInfo.Builder()
                    .setTitle(context.getString(R.string.AuthorisationNeeded))
                    .setConfirmationRequired(false)
                    .setNegativeButtonText(context.getString(R.string.Cancel))
                    .build())
        }
    }

    companion object {
        @Volatile private var instance: FingerprintAuth? = null
        private val LOCK = Any()

        /**
         * Funkcja zwracająca i generująca obiekt menadżera
         * @return menadżer autoryzacji odciskiem palca
         */
        fun getInstance(context: Context) = instance ?: synchronized(LOCK){
            instance ?: FingerprintAuth(context).also { instance = it}
        }
    }
}