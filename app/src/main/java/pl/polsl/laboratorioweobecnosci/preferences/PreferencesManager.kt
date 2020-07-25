package pl.polsl.laboratorioweobecnosci.preferences

import android.content.Context
import android.graphics.Color
import android.os.Environment
import android.os.Environment.MEDIA_MOUNTED
import androidx.preference.PreferenceManager
import pl.polsl.laboratorioweobecnosci.R

/**
 * Menadżer ustawień aplikacji
 * @param context context aktywności wywołującej
 * @property preferences obiekt zapisujący i odczytujący ustawienia aplikacji z urządzenia
 * @property instance zmienna przechowująca globalny obiekt menadżera
 * @property DEFAULT_UNRATED_COLOR domyślny kolor przycisku nieocenionego stanowiska
 * @property DEFAULT_RATED_COLOR domyślny kolor przycisku ocenionego stanowiska
 * @property DEFAULT_FORCED_GRADE_COLOR domyślny kolor przycisku stanowiska z wymuszoną oceną
 * @property LOCK blokada zmiennej globalnej
 */
class PreferencesManager(private val context: Context) {

    private val preferences = PreferenceManager.getDefaultSharedPreferences(context)

    private val DEFAULT_UNRATED_COLOR = Color.LTGRAY
    private val DEFAULT_RATED_COLOR = Color.GREEN
    private val DEFAULT_FORCED_GRADE_COLOR = Color.YELLOW

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
     * Funkcja zwracająca kolor dla nieocenionego stanowiska
     * @return kolor nieocenionego stanowiska
     */
    fun unratedWorkstationIntColor(): Int {
        if (preferences.contains(context.getString(R.string.unrated_workstation_key))) {
            return preferences.getInt(context.getString(R.string.unrated_workstation_key), DEFAULT_UNRATED_COLOR)
        } else
            return DEFAULT_UNRATED_COLOR
    }

    /**
     * Funkcja zwracająca kolor dla ocenionego stanowiska
     * @return kolor ocenionego stanowiska
     */
    fun ratedWorkstationIntColor(): Int {
        if (preferences.contains(context.getString(R.string.rated_workstation_key))) {
            return preferences.getInt(context.getString(R.string.rated_workstation_key), DEFAULT_RATED_COLOR)
        } else
            return DEFAULT_RATED_COLOR
    }

    /**
     * Funkcja zwracająca kolor dla stanowiska z wymuszoną oceną
     * @return kolor dla stanowiska z wymuszoną oceną
     */
    fun forcedGradeIntColor(): Int {
        if (preferences.contains(context.getString(R.string.forced_grade_key))) {
            return preferences.getInt(context.getString(R.string.forced_grade_key), DEFAULT_FORCED_GRADE_COLOR)
        } else
            return DEFAULT_FORCED_GRADE_COLOR
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