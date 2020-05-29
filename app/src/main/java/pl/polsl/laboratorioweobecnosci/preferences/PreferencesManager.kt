package pl.polsl.laboratorioweobecnosci.preferences

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.DropDownPreference
import androidx.preference.Preference
import androidx.preference.PreferenceManager

class PreferencesManager(private val context: Context) {

    private val preferences = PreferenceManager.getDefaultSharedPreferences(context)

    fun authMode(): AuthorizationMode {
        var res = AuthorizationMode.NONE
        if (preferences.contains("auth_method")) {
            val authStr = preferences.getString("auth_method", "")
            if (authStr != null)
                res = AuthorizationMode.fromInt(authStr.toInt())
        }
        return res
    }

    companion object {
        lateinit var instance: PreferencesManager
    }
}