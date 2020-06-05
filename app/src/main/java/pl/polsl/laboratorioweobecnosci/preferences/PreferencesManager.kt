package pl.polsl.laboratorioweobecnosci.preferences

import android.content.Context
import android.os.Environment
import android.os.Environment.MEDIA_MOUNTED
import androidx.preference.PreferenceManager
import pl.polsl.laboratorioweobecnosci.R

class PreferencesManager(private val context: Context) {

    private val preferences = PreferenceManager.getDefaultSharedPreferences(context)

    fun saveCSVPath(): String {
        if (preferences.contains(context.getString(R.string.save_csv_path_key))) {
            return preferences.getString(context.getString(R.string.save_csv_path_key), context.filesDir.path)!!
        } else if (Environment.getExternalStorageState() == MEDIA_MOUNTED) {
            return Environment.getExternalStorageDirectory().path
        } else {
            return context.filesDir.path
        }
    }

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
        fun isInitialized(): Boolean { return this::instance.isInitialized }
    }
}