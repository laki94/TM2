package pl.polsl.laboratorioweobecnosci.activities.admin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import pl.polsl.laboratorioweobecnosci.R

class RateActivity : AppCompatActivity() {

    private lateinit var mAdapter: RateListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rate)

        mAdapter = RateListAdapter(this)

        val list = findViewById<RecyclerView>(R.id.rvWorkstations)
        list.layoutManager = LinearLayoutManager(this)
        list.adapter = mAdapter
    }

    fun onSaveRatesClick(view: View) {
        /* TODO */
    }
}
