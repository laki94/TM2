package pl.polsl.laboratorioweobecnosci.preferences

import android.content.Context
import android.os.Environment
import android.os.Environment.MEDIA_MOUNTED
import androidx.preference.PreferenceManager
import pl.polsl.laboratorioweobecnosci.R

/**
 * Menadżer ustawień aplikacji
 * @param context context aktywności wywołującej
 * @property preferences obiekt zapisujący i odczytujący ustawienia aplikacji z urządzenia
 * @property instance zmienna przechowująca globalny obiekt menadżera
 * @property LOCK blokada zmiennej globalnej
 */
class PreferencesManager(private val context: Context) {

    private val preferences = PreferenceManager.getDefaultSharedPreferences(context)

    /**
     * Funkcja zwracająca ścieżkę do zapisu plików CSV
     * @return wybrana ścieżka zapisu plików CSV
     */
    fun saveCSVPath(): String {
        if (preferences.contains(context.getString(R.string.save_csv_path_key))) {
            return preferences.getString(context.getString(R.string.save_csv_path_key), context.filesDir.path)!!
        } else if (Environment.getExternalStorageState() == MEDIA_MOUNTED) {
            return Environment.getExternalStorageDirectory().path
        } else {
            return context.filesDir.path
        }
    }

    /**
     * Funkcja zwracająca wybraną metodę autoryzacji
     * @return wybrana metoda autoryzacji
     */
    fun chosenAuthorizationMethod(): AuthorizationMode {
        return AuthorizationMode.fromInt(
            preferences.getString(
                context.getString(R.string.authorization_method_key),
                context.getString(R.string.none_value))!!.toInt())
    }

    /**
     * Funkcja zwracająca zapisane hasło autoryzacji
     * @return zahashowane hasło
     */
    fun hashedPassword(): String {
        return preferences.getString(context.getString(R.string.user_password_key), "")!!
    }

    /**
     * Funkcja zwracająca czy opcjonalna autoryzacja jest włączona
     * @return True jeśli opcjonalna autoryzacja jest włączona
     */
    fun optionalAuthorizationEnabled(): Boolean {
        return preferences.getBoolean(context.getString(R.string.allow_optional_auth_key), false)
    }

    companion object {
        @Volatile private var instance: PreferencesManager? = null
        private val LOCK = Any()

        /**
         * Funkcja zwracająca i generująca obiekt menadżera
         * @return menadżer ustawień
         */
        fun getInstance(context: Context) = instance ?: synchronized(LOCK){
            instance ?: PreferencesManager(context).also { instance = it}
        }
    }
}