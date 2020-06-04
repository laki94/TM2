package pl.polsl.laboratorioweobecnosci.activities.admin

import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import pl.polsl.laboratorioweobecnosci.R
import pl.polsl.laboratorioweobecnosci.preferences.PermissionsManager

class PreferencesActivity : AppCompatActivity() {

    private val preferencesFragment = MyPreferencesFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_preferences)
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.preferencesFragment, preferencesFragment)
            .commit()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            PermissionsManager.READ_EXTERNAL_STORAGE_REQ_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    preferencesFragment.onReadExternalPermissionGranted()
                } else {
                    preferencesFragment.onReadExternalPermissionCanceled()
                }
            }
            PermissionsManager.WRITE_EXTERNAL_STORAGE_REQ_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    preferencesFragment.onWriteExternalPermissionGranted()
                } else {
                    preferencesFragment.onWriteExternalPermissionCanceled()
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}
