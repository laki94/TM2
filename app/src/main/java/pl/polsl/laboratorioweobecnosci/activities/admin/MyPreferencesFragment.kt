package pl.polsl.laboratorioweobecnosci.activities.admin

import android.os.Bundle
import androidx.preference.DropDownPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreferenceCompat
import pl.polsl.laboratorioweobecnosci.R
import pl.polsl.laboratorioweobecnosci.security.FingerprintAuth

class MyPreferencesFragment: PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)

        setAuthenticationEnabled()
    }

    private fun setAuthenticationEnabled() {
        val authEnabled = findPreference<SwitchPreferenceCompat>(getString(R.string.auth_enabled_key))
        val authentication = FingerprintAuth(requireContext())
        authEnabled?.isEnabled = authentication.isAvailable()
    }
}