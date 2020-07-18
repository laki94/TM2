package pl.polsl.laboratorioweobecnosci.activities.admin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import pl.polsl.laboratorioweobecnosci.R
import pl.polsl.laboratorioweobecnosci.database.DatabaseHandler
import pl.polsl.laboratorioweobecnosci.database.models.lists.LaboratoryList

/**
 * Aktywność administratora pozwalająca na zarządzanie laboratoriami, ocenami i ustawieniami aplikacji
 */
class AdminActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_panel)
    }

    /**
     * Funkcja wywołana po wciśnięciu przycisku Ćwiczenia, otwiera nową aktywność wyświetlającą
     * wszystkie laboratoria.
     */
    fun onExercisesClick(view: View) {
        val intent = Intent(this, LaboratoriesActivity::class.java)
        startActivity(intent)
    }

    /**
     * Funkcja wywołana po wciśnięciu przycisku Oceniaj, otwiera dialog wyświetlający
     * wszystkie laboratoria do oceniania studentów na stanowiskach. Nie otworzy dialogu jeśli
     * nie ma laboratoriów do wyświetlenia.
     */
    fun onRateClick(view: View) {
        Thread {
            val db = DatabaseHandler(this)
            val laboratories = db.getLaboratoriesSortedByStartDate()
            runOnUiThread {
                if (laboratories.isEmpty()) {
                    Toast.makeText(this, R.string.NoLaboratoriesToShow, Toast.LENGTH_LONG).show()
                } else {
                    val dialog = LaboratoriesDialog(this)
                    dialog.showLaboratoriesForRating(layoutInflater, laboratories)
                }
            }
        }.start()
    }

    /**
     * Funkcja wywołana po wciśnięciu przycisku Generuj CSV, otwiera nową aktywność
     * wyświetlającą wszystkie laboratoria dla których można wygenerować laboratoria
     */
    fun onGenerateCSVClick(view: View) {
        val intent = Intent(this, GenerateCSVActivity::class.java)
        startActivity(intent)
    }

    /**
     * Funkcja wywołana po wciśnięciu przycisku Ustawień, otwiera aktywność z ustawieniami
     * aplikacji
     */
    fun onAdminPreferencesClick(view: View) {
        val intent = Intent(this, PreferencesActivity::class.java)
        startActivity(intent)
    }
}
