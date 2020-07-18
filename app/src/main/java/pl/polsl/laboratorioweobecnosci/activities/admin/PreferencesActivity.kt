package pl.polsl.laboratorioweobecnosci.activities.admin

import android.os.Bundle
import pl.polsl.laboratorioweobecnosci.R
import pl.polsl.laboratorioweobecnosci.activities.BaseActivity

/**
 * Aktywność z ustawieniami aplikacji
 * @property preferencesFragment fragment z ustawieniami
 */
class PreferencesActivity : BaseActivity() {

    private val preferencesFragment = MyPreferencesFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_preferences)
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.preferencesFragment, preferencesFragment)
            .commit()
    }
}
