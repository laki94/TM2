package pl.polsl.laboratorioweobecnosci.security

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import pl.polsl.laboratorioweobecnosci.R
import java.util.concurrent.Executor


class FingerprintAuth(private val context: Context) {
    private val TAG = "FINGERPRINTAUTH"
    private val biometricManager = BiometricManager.from(context)
    private lateinit var executor: Executor

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

    fun authorize(activity: AppCompatActivity, callbackObject: BiometricPrompt.AuthenticationCallback) {
        executor = ContextCompat.getMainExecutor(context)

        val prompt = BiometricPrompt(activity, executor, callbackObject)
        prompt.authenticate(
            BiometricPrompt.PromptInfo.Builder()
                .setTitle(context.getString(R.string.AuthorisationNeeded))
                .setConfirmationRequired(false)
                .setDeviceCredentialAllowed(true)
                .build()
        )
    }
}