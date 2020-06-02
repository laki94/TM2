package pl.polsl.laboratorioweobecnosci.activities.admin

import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.widget.Toast
import androidx.preference.DropDownPreference
import androidx.preference.Preference
import androidx.preference.PreferenceViewHolder
import androidx.preference.SwitchPreferenceCompat
import com.takisoft.preferencex.EditTextPreference
import com.takisoft.preferencex.PreferenceFragmentCompat
import org.mindrot.jbcrypt.BCrypt
import pl.polsl.laboratorioweobecnosci.R
import pl.polsl.laboratorioweobecnosci.preferences.AuthorizationMode
import pl.polsl.laboratorioweobecnosci.preferences.PreferencesManager
import pl.polsl.laboratorioweobecnosci.security.FingerprintAuth

class MyPreferencesFragment: PreferenceFragmentCompat(), Preference.OnPreferenceChangeListener {

    private val disabledMethods = ArrayList<AuthorizationMode>()
    private lateinit var authModes: DropDownPreference
    private lateinit var passEdit: EditTextPreference
    private lateinit var optAuth: SwitchPreferenceCompat
    private var chosenAuthMethod = AuthorizationMode.NONE

    override fun onPreferenceChange(
        preference: Preference?,
        newValue: Any?
    ): Boolean {
        if (preference?.key.equals(getString(R.string.authorization_method_key))) {
            if (newValue is String) {
                chosenAuthMethod = AuthorizationMode.fromInt(newValue.toString().toInt())
                 setControlsVisibility()
            }
        } else if (preference?.key.equals(getString(R.string.user_password_key))) {
            if (newValue.toString().isEmpty()) {
                Toast.makeText(context, R.string.PasswordCannotBeEmpty, Toast.LENGTH_LONG).show()
            } else {
                passEdit.summary = getString(R.string.Set)
                val hashed = BCrypt.hashpw(newValue.toString(), BCrypt.gensalt())
                with (preference?.sharedPreferences?.edit()!!) {
                    putString(getString(R.string.user_password_key), hashed)
                    commit()
                }
            }
            return false
        }
        return true
    }

    override fun onCreatePreferencesFix(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)

        setAuthenticationMethods()

        authModes.onPreferenceChangeListener = this
        passEdit = findPreference(getString(R.string.user_password_key))!!
        passEdit.setOnBindEditTextListener {
            when(chosenAuthMethod) {
                AuthorizationMode.PIN -> {
                    it.inputType = InputType.TYPE_NUMBER_VARIATION_PASSWORD + InputType.TYPE_CLASS_NUMBER
                }
                else -> {
                    it.inputType = InputType.TYPE_TEXT_VARIATION_PASSWORD + InputType.TYPE_CLASS_TEXT
                }
            }
            it.setText("")
        }

        passEdit.onPreferenceChangeListener = this

        if (PreferencesManager.instance.hashedPassword().isEmpty()) {
            passEdit.summary = getString(R.string.NotSet)
        } else {
            passEdit.summary = getString(R.string.Set)
        }

        optAuth = findPreference(getString(R.string.allow_optional_auth_key))!!

        chosenAuthMethod = PreferencesManager.instance.chosenAuthorizationMethod()
        setControlsVisibility()
    }

    private fun setControlsVisibility() {
        when (chosenAuthMethod) {
            AuthorizationMode.NONE -> {
                passEdit.isVisible = false
                optAuth.isVisible = false
            }

            AuthorizationMode.PIN -> {
                passEdit.isVisible = true
                optAuth.isVisible = false
            }

            AuthorizationMode.PASSWORD -> {
                passEdit.isVisible = true
                optAuth.isVisible = false
            }

            AuthorizationMode.FINGERPRINT -> {
                passEdit.isVisible = false
                optAuth.isVisible = true
            }
        }
    }

    private fun setAuthenticationMethods() {
        authModes = findPreference(getString(R.string.authorization_method_key))!!
        val authentication = FingerprintAuth(requireContext())

        if (!authentication.isAvailable()) {
            disabledMethods.add(AuthorizationMode.FINGERPRINT)
        }

        authModes.entries = getAuthorizationMethods()
        authModes.entryValues = getAuthorizationMethodsValues()
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