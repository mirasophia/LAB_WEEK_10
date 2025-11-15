package com.example.lab_week_10

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import com.example.lab_week_10.database.Total
import com.example.lab_week_10.database.TotalDatabase
import com.example.lab_week_10.viewmodels.TotalViewModel

class MainActivity : AppCompatActivity() {

    // lazy DB creation
    private val db: TotalDatabase by lazy { prepareDatabase() }

    private val viewModel by lazy {
        ViewModelProvider(this)[TotalViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // initialize value from DB (must be called before preparing VM observer so UI show correct initial)
        initializeValueFromDatabase()

        prepareViewModel()
    }

    private fun updateText(total: Int) {
        findViewById<TextView>(R.id.text_total).text =
            getString(R.string.text_total, total)
    }

    private fun prepareViewModel() {
        // Observe LiveData from ViewModel
        viewModel.total.observe(this) { total ->
            updateText(total)
        }

        findViewById<Button>(R.id.button_increment).setOnClickListener {
            viewModel.incrementTotal()
        }
    }

    // build Room DB (as in module, using allowMainThreadQueries for simplicity)
    private fun prepareDatabase(): TotalDatabase {
        return Room.databaseBuilder(
            applicationContext,
            TotalDatabase::class.java,
            "total-database"
        ).allowMainThreadQueries().build()
    }

    // read existing value or insert initial one
    private fun initializeValueFromDatabase() {
        val totals = db.totalDao().getTotal(ID)
        if (totals.isEmpty()) {
            // insert initial row with id = 1 and total = 0
            db.totalDao().insert(Total(id = ID, total = 0))
            viewModel.setTotal(0)
        } else {
            viewModel.setTotal(totals.first().total)
        }
    }

    // save to DB on pause
    override fun onPause() {
        super.onPause()
        val current = viewModel.total.value ?: 0
        db.totalDao().update(Total(ID, current))
    }

    override fun onStart() {
        super.onStart()
        // optional: show toast for debugging
        val current = viewModel.total.value ?: 0
        Toast.makeText(this, "Current total: $current", Toast.LENGTH_SHORT).show()
    }

    companion object {
        const val ID: Long = 1L
    }
}
