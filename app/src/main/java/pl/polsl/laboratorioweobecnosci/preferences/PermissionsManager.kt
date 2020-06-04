package pl.polsl.laboratorioweobecnosci.preferences

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat

class PermissionsManager {

    fun haveReadExternalPermission(context: Context): Boolean {
        return (ActivityCompat.checkSelfPermission(context, READ_EXTERNAL_STORAGE)) == PackageManager.PERMISSION_GRANTED
    }

    fun askForReadExternalPermission(activity: Activity) {
        ActivityCompat.requestPermissions(activity,
                        arrayOf(READ_EXTERNAL_STORAGE),
                        READ_EXTERNAL_STORAGE_REQ_CODE)
    }

    fun haveWriteExternalPermission(context: Context): Boolean {
        return (ActivityCompat.checkSelfPermission(context, WRITE_EXTERNAL_STORAGE)) == PackageManager.PERMISSION_GRANTED
    }

    fun askForWriteExternalPermission(context: Context) {
        ActivityCompat.requestPermissions(context as Activity,
            arrayOf(WRITE_EXTERNAL_STORAGE),
            WRITE_EXTERNAL_STORAGE_REQ_CODE)
    }

    companion object {
        val instance = PermissionsManager()

        const val READ_EXTERNAL_STORAGE_REQ_CODE = 1
        const val WRITE_EXTERNAL_STORAGE_REQ_CODE = 2
    }
}