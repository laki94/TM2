package pl.polsl.laboratorioweobecnosci.activities.admin

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import org.mindrot.jbcrypt.BCrypt
import pl.polsl.laboratorioweobecnosci.R
import pl.polsl.laboratorioweobecnosci.preferences.PreferencesManager

/**
 * Dialog z zapytaniem o hasło autoryzacji
 * @param context context aktywności
 * @property dialogLayout dialog który zostanie wyświetlony
 * @property mainPasswordEdit pole tekstowe z hasłem
 * @property onAuthorized callback, który zostaje wywołany po zautoryzowaniu użytkownika
 */
class PasswordDialog(context: Context): AlertDialog.Builder(context) {

    private lateinit var dialogLayout: View
    private lateinit var mainPasswordEdit: EditText

    private lateinit var onAuthorized: () -> Unit

    /**
     * Przypisanie pola tekstowego do zmiennej
     */
    private fun getPasswordEdits() {
        mainPasswordEdit = dialogLayout.findViewById(R.id.etMainPassword)
    }

    /**
     * Zapytanie użytkownika o PIN
     * @param inflater LayoutInflater aktywności wywołującej
     * @param doOnPasswordValid Callback do wywołania po zautoryzowaniu
     */
    fun askForPin(inflater: LayoutInflater, doOnPasswordValid: () -> (Unit)) {
        if (PreferencesManager.instance.hashedPassword().isEmpty()) {
            doOnPasswordValid()
        } else {
            dialogLayout = inflater.inflate(R.layout.dialog_password, null)
            onAuthorized = doOnPasswordValid
            getPasswordEdits()
            mainPasswordEdit.inputType = InputType.TYPE_NUMBER_VARIATION_PASSWORD + InputType.TYPE_CLASS_NUMBER
            doShowDialog()
        }
    }

    /**
     * Wyświetlenie dialogu z wymaganą autoryzacją
     */
    private fun doShowDialog() {
        setTitle(R.string.AuthorisationNeeded)
        setView(dialogLayout)
        setPositiveButton(R.string.Save) { _, _ ->

        }
        val dialog = create()
        dialog.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)
        dialog.show()

        val btn = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
        btn.setOnClickListener {
            if (comparePassword()) {
                onAuthorized()
                dialog.dismiss()
            } else {
                Toast.makeText(context, R.string.WrongPassword, Toast.LENGTH_LONG).show()
            }
        }
    }

    /**
     * Zapytanie użytkownika o hasło
     * @param inflater LayoutInflater aktywności wywołującej
     * @param doOnPasswordValid Callback do wywołania po zautoryzowaniu
     */
    fun askForPassword(inflater: LayoutInflater, doOnPasswordValid: () -> Unit) {
        if (PreferencesManager.instance.hashedPassword().isEmpty()) {
            doOnPasswordValid()
        } else {
            dialogLayout = inflater.inflate(R.layout.dialog_password, null)
            onAuthorized = doOnPasswordValid

            getPasswordEdits()
            doShowDialog()
        }
    }

    /**
     * Sprawdzanie poprawności wprowadzonego hasła
     * @return True jeśli hasło jest poprawne
     */
    private fun comparePassword(): Boolean {
        return BCrypt.checkpw(mainPasswordEdit.text.toString(), PreferencesManager.instance.hashedPassword())
    }
}
