package pl.polsl.laboratorioweobecnosci.activities.admin

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.widget.Toast
import androidx.preference.*
import com.aditya.filebrowser.Constants
import com.aditya.filebrowser.FolderChooser
import com.takisoft.preferencex.EditTextPreference
import com.takisoft.preferencex.PreferenceFragmentCompat
import org.mindrot.jbcrypt.BCrypt
import pl.polsl.laboratorioweobecnosci.R
import pl.polsl.laboratorioweobecnosci.preferences.AuthorizationMode
import pl.polsl.laboratorioweobecnosci.preferences.PreferencesManager
import pl.polsl.laboratorioweobecnosci.security.FingerprintAuth
import java.io.File

/**
 * Fragment wyświetlający ustawienia aplikacji
 * @property disabledMethods tablica typów autoryzacji które nie są dostępne
 * @property authModes ustawienie typu autoryzacji
 * @property passEdit ustawienie hasła autoryzacji
 * @property optAuth ustawienie alternatywnej autoryzacji dla autoryzacji palcem
 * @property csvSavePath ustawienie domyślnej ścieżki zapisu plików CSV
 * @property chosenAuthMethod aktualnie wybrana metoda autoryzacji
 */
class MyPreferencesFragment: PreferenceFragmentCompat(), Preference.OnPreferenceChangeListener {

    private val disabledMethods = ArrayList<AuthorizationMode>()
    private lateinit var authModes: DropDownPreference
    private lateinit var passEdit: EditTextPreference
    private lateinit var optAuth: SwitchPreferenceCompat
    private lateinit var csvSavePath: Preference
    private var chosenAuthMethod = AuthorizationMode.NONE
    private val CHANGE_CSV_PATH_REQ_CODE = 123

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

        preparePasswordEdit()
        prepareOptionalAuthorization()
        prepareCSVSavePath()

        chosenAuthMethod = PreferencesManager.instance.chosenAuthorizationMethod()
        setControlsVisibility()
    }

    /**
     * Przygotowanie ustawienia ścieżki zapisu plików CSV
     */
    private fun prepareCSVSavePath() {
        csvSavePath = findPreference(getString(R.string.save_csv_path_key))!!
        csvSavePath.summary = PreferencesManager.instance.saveCSVPath()
        csvSavePath.onPreferenceClickListener = Preference.OnPreferenceClickListener {
            openSelectDirectoryDialog()
            return@OnPreferenceClickListener true
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            CHANGE_CSV_PATH_REQ_CODE -> {
                if (resultCode == Activity.RESULT_OK) {
                    val newPath = data?.data?.path
                    with(csvSavePath.sharedPreferences.edit()) {
                        putString(getString(R.string.save_csv_path_key), newPath)
                        apply()
                    }
                    csvSavePath.summary = newPath
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    /**
     * Otworzenie dialogu z wyborem katalogu, w którym mają być zapisane pliki CSV
     */
    private fun openSelectDirectoryDialog() {
        val intent = Intent(requireContext(), FolderChooser::class.java)
        intent.putExtra(Constants.SELECTION_MODE, Constants.SELECTION_MODES.SINGLE_SELECTION.ordinal)
        intent.putExtra(Constants.INITIAL_DIRECTORY, File(PreferencesManager.instance.saveCSVPath()).absolutePath)
        startActivityForResult(intent, CHANGE_CSV_PATH_REQ_CODE)
    }

    /**
     * Przygotowanie ustawienia opcjonalnej autoryzacji
     */
    private fun prepareOptionalAuthorization() {
        optAuth = findPreference(getString(R.string.allow_optional_auth_key))!!
    }

    /**
     * Przygotowanie ustawienia hasła autoryzacji
     */
    private fun preparePasswordEdit() {
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
    }

    /**
     * Ustawienie widoczności ustawień
     */
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

    /**
     * Przygotowanie metod autoryzacji
     */
    private fun setAuthenticationMethods() {
        authModes = findPreference(getString(R.string.authorization_method_key))!!

        if (!FingerprintAuth.instance.isAvailable()) {
            disabledMethods.add(AuthorizationMode.FINGERPRINT)
        }

        authModes.entries = getAuthorizationMethods()
        authModes.entryValues = getAuthorizationMethodsValues()
    }

    /**
     * Pobranie dostępnych metod autoryzacji
     * @return tablica metod autoryzacji
     */
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

    /**
     * Pobranie wartości przypisanych metodom autoryzacji
     * @return tablica wartości odpowiadających metodom autoryzacji
     */
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