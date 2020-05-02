package pl.polsl.laboratorioweobecnosci.activities.admin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import pl.polsl.laboratorioweobecnosci.R
import pl.polsl.laboratorioweobecnosci.models.Workstation
import pl.polsl.laboratorioweobecnosci.models.WorkstationsList

class RateActivity : AppCompatActivity() {

    private lateinit var mAdapter: RateListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rate)

        mAdapter = RateListAdapter(this, WorkstationsList())

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
        dialog.rate(layoutInflater, Workstation(1)) //TODO
    }

    fun onSaveRatesClick(view: View) {
        /* TODO */
    }
}
