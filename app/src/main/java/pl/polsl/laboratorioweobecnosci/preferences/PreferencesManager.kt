package pl.polsl.laboratorioweobecnosci.preferences

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.DropDownPreference
import androidx.preference.Preference
import androidx.preference.PreferenceManager
import pl.polsl.laboratorioweobecnosci.R

class PreferencesManager(private val context: Context) {

    private val preferences = PreferenceManager.getDefaultSharedPreferences(context)

    fun isAuthorizationNeeded(): Boolean {
        return preferences.getBoolean(context.getString(R.string.auth_enabled_key), false)
    }

    companion object {
        lateinit var instance: PreferencesManager
    }
}