package pl.polsl.laboratorioweobecnosci.activities.admin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import pl.polsl.laboratorioweobecnosci.R
import pl.polsl.laboratorioweobecnosci.database.models.*

class RateActivity : AppCompatActivity() {

    private lateinit var mAdapter: RateListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rate)

        mAdapter = RateListAdapter(this, StudentWorkstationLaboratoryList())

        mAdapter.let {
            it.onWorkstationClick = {
                showRateSingleWorkstationDialog();
            }
        }

        val list = findViewById<RecyclerView>(R.id.rvWorkstations)
        list.layoutManager = LinearLayoutManager(this)
        list.adapter = mAdapter
    }

    private fun showRateSingleWorkstationDialog() {
        val dialog = RateWorkstationDialog(this)
        //Tu jest zły dialog
//        dialog.rate(layoutInflater, StudentWorkstationLaboratory(Workstation(1), StudentsList(this.studentDao().getStudentsOnWorkstation(laboratoryId, it)))) //TODO
    }

    fun onSaveRatesClick(view: View) {
        /* TODO */
    }
}
