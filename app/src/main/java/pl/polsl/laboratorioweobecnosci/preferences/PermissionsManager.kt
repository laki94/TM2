package pl.polsl.laboratorioweobecnosci.preferences

import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat

/**
 * Menadżer uprawnień sprawdzający czy aplikacja posiada wymagane uprawnienia
 * @property instance zmienna przechowująca globalny obiekt menadżera
 * @property LOCK blokada zmiennej globalnej
 */
class PermissionsManager {

    /**
     * Sprawdzenie czy aplikacja ma uprawnienie do zapisu na zewnętrznej pamięci
     * @param context context aktywności wywołującej
     * @return True jeśli aplikacja ma uprawnienie
     */
    fun haveWriteExternalPermission(context: Context): Boolean {
        return (ActivityCompat.checkSelfPermission(context, WRITE_EXTERNAL_STORAGE)) == PackageManager.PERMISSION_GRANTED
    }

    /**
     * Wyświetlenie zapytania o uprawnienie do zapisu na zewnętrznej pamięci
     * @param context context aktywności wywołującej
     */
    fun askForWriteExternalPermission(context: Context) {
        ActivityCompat.requestPermissions(context as Activity,
            arrayOf(WRITE_EXTERNAL_STORAGE),
            WRITE_EXTERNAL_STORAGE_REQ_CODE)
    }

    companion object {
        @Volatile private var instance: PermissionsManager? = null
        private val LOCK = Any()

        /**
         * Funkcja zwracająca i generująca obiekt menadżera
         * @return menadżer uprawnień
         */
        fun getInstance() = instance ?: synchronized(LOCK){
            instance ?: PermissionsManager().also { instance = it}
        }
        const val WRITE_EXTERNAL_STORAGE_REQ_CODE = 2
    }
}