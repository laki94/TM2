package pl.polsl.laboratorioweobecnosci.activities.admin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import pl.polsl.laboratorioweobecnosci.R

class PreferencesActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_preferences)
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.preferencesFragment, MyPreferencesFragment())
            .commit()
    }
}
