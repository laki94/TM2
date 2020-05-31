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
import androidx.appcompat.app.AlertDialog
import pl.polsl.laboratorioweobecnosci.R

class PasswordDialog(context: Context): AlertDialog.Builder(context) {

    private lateinit var dialogLayout: View
    private lateinit var mainPasswordEdit: EditText
    private lateinit var secondPasswordEdit: EditText

    private fun getPasswordEdits() {
        mainPasswordEdit = dialogLayout.findViewById(R.id.etMainPassword)
        secondPasswordEdit = dialogLayout.findViewById(R.id.etSecondPassword)
    }

    fun askForPin(inflater: LayoutInflater) {
        dialogLayout = inflater.inflate(R.layout.dialog_password, null)

        getPasswordEdits()

        mainPasswordEdit.inputType = InputType.TYPE_NUMBER_VARIATION_PASSWORD + InputType.TYPE_CLASS_NUMBER
        secondPasswordEdit.visibility = View.GONE

        setTitle(R.string.AuthorisationNeeded)
        setView(dialogLayout)
        setPositiveButton(R.string.Save) { _, _ ->
//            fillLaboratory()
//            onSaveClick?.invoke(mainLaboratory)
        }
        val dialog = create()
        dialog.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)
        dialog.show()
    }

    fun askForPassword(inflater: LayoutInflater) {
        dialogLayout = inflater.inflate(R.layout.dialog_password, null)

        getPasswordEdits()
        secondPasswordEdit.visibility = View.GONE

        setTitle(R.string.AuthorisationNeeded)
        setView(dialogLayout)
        setPositiveButton(R.string.Save) { _, _ ->
//            fillLaboratory()
//            onSaveClick?.invoke(mainLaboratory)
        }
        val dialog = create()
        dialog.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)
        dialog.show()
    }
}
