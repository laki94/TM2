package pl.polsl.laboratorioweobecnosci.activities.admin

import android.os.Bundle
import androidx.preference.DropDownPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreferenceCompat
import pl.polsl.laboratorioweobecnosci.R
import pl.polsl.laboratorioweobecnosci.preferences.AuthorizationMode
import pl.polsl.laboratorioweobecnosci.security.FingerprintAuth

class MyPreferencesFragment: PreferenceFragmentCompat() {

    private val disabledMethods = ArrayList<AuthorizationMode>()

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)

        setAuthenticationMethods()
    }

    private fun setAuthenticationMethods() {
        val authModes = findPreference<DropDownPreference>(getString(R.string.authorization_method_key))
        val authentication = FingerprintAuth(requireContext())

        if (!authentication.isAvailable()) {
            disabledMethods.add(AuthorizationMode.FINGERPRINT)
        }

        authModes?.entries = getAuthorizationMethods()
        authModes?.entryValues = getAuthorizationMethodsValues()
    }

    private fun getAuthorizationMethods(): Array<String> {
        val res = ArrayList<String>()
        res.add(getString(R.string.None))
        res.add(getString(R.string.PIN))
        res.add(getString(R.string.Password))
        if (AuthorizationMode.FINGERPRINT !in disabledMethods) {
            res.add(getString(R.string.Fingerprint))
        }
        return res.toTypedArray()
    }

    private fun getAuthorizationMethodsValues(): Array<String> {
        val res = ArrayList<String>()
        res.add(getString(R.string.none_value))
        res.add(getString(R.string.pin_value))
        res.add(getString(R.string.password_value))
        if (AuthorizationMode.FINGERPRINT !in disabledMethods) {
            res.add(getString(R.string.fingerprint_value))
        }
        return res.toTypedArray()
    }
}