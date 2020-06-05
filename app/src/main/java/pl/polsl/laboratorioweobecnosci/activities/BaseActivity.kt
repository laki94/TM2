package pl.polsl.laboratorioweobecnosci.activities

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import pl.polsl.laboratorioweobecnosci.preferences.PreferencesManager
import pl.polsl.laboratorioweobecnosci.security.FingerprintAuth

@SuppressLint("Registered")
open class BaseActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        if (!PreferencesManager.isInitialized()) {
            PreferencesManager.instance = PreferencesManager(this)
        }
        if (!FingerprintAuth.isInitialized()) {
            FingerprintAuth.instance = FingerprintAuth(this)
        }

        super.onCreate(savedInstanceState)
    }
}