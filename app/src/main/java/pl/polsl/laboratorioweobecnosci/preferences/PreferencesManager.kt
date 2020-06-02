package pl.polsl.laboratorioweobecnosci.preferences

import android.content.Context
import androidx.preference.PreferenceManager
import pl.polsl.laboratorioweobecnosci.R

class PreferencesManager(private val context: Context) {

    private val preferences = PreferenceManager.getDefaultSharedPreferences(context)

    fun chosenAuthorizationMethod(): AuthorizationMode {
        return AuthorizationMode.fromInt(
            preferences.getString(
                context.getString(R.string.authorization_method_key),
                context.getString(R.string.none_value))!!.toInt())
    }

    fun hashedPassword(): String {
        return preferences.getString(context.getString(R.string.user_password_key), "")!!
    }

    fun optionalAuthorizationEnabled(): Boolean {
        return preferences.getBoolean(context.getString(R.string.allow_optional_auth_key), false)
    }

    companion object {
        lateinit var instance: PreferencesManager
    }
}