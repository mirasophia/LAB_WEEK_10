package com.example.lab_week_10

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import com.example.lab_week_10.database.Total
import com.example.lab_week_10.database.TotalDatabase
import com.example.lab_week_10.utils.ToastUtil
import com.example.lab_week_10.viewmodels.TotalViewModel
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    private val TAG = "MainActivity"
    private val db: TotalDatabase by lazy { prepareDatabase() }
    private val viewModel by lazy { ViewModelProvider(this)[TotalViewModel::class.java] }

    private var textTotal: TextView? = null
    private var textUpdated: TextView? = null
    private var buttonIncrement: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        textTotal = findViewById(R.id.text_total)
        textUpdated = findViewById(R.id.text_updated)
        buttonIncrement = findViewById(R.id.button_increment)

        try {
            initializeValueFromDatabase()
        } catch (e: Exception) {
            Log.e(TAG, "DB init error", e)
            ToastUtil.showCustomToast(this, "DB init error", e.message ?: "")
        }

        prepareViewModel()
    }

    private fun prepareViewModel() {
        viewModel.total.observe(this) { total ->
            textTotal?.text = getString(R.string.text_total, total)
        }

        viewModel.lastUpdated.observe(this) { epoch ->
            if (epoch <= 0L) {
                textUpdated?.text = getString(R.string.text_updated_never)
            } else {
                textUpdated?.text = getString(R.string.text_updated_at, formatDate(epoch))
            }
        }

        buttonIncrement?.setOnClickListener {
            try {
                viewModel.incrementTotal()
                val current = viewModel.total.value ?: 0
                val now = System.currentTimeMillis()
                viewModel.setLastUpdated(now)
                // persist immediately
                db.totalDao().update(Total(ID, current, now))
                // show custom toast (timestamp as main line)
                ToastUtil.showCustomToast(this, formatDate(now), "")
            } catch (e: Exception) {
                Log.e(TAG, "Error on increment", e)
                ToastUtil.showCustomToast(this, "Error on increment", e.message ?: "")
            }
        }
    }

    private fun prepareDatabase(): TotalDatabase {
        return Room.databaseBuilder(
            applicationContext,
            TotalDatabase::class.java,
            "total-database"
        )
            .fallbackToDestructiveMigration()
            .allowMainThreadQueries()
            .build()
    }

    private fun initializeValueFromDatabase() {
        val totals = db.totalDao().getTotal(ID)
        if (totals.isEmpty()) {
            db.totalDao().insert(Total(id = ID, total = 0, lastUpdated = 0L))
            viewModel.setTotal(0)
            viewModel.setLastUpdated(0L)
        } else {
            val t = totals.first()
            viewModel.setTotal(t.total)
            viewModel.setLastUpdated(t.lastUpdated)
        }
    }

    override fun onPause() {
        super.onPause()
        val current = viewModel.total.value ?: 0
        val last = viewModel.lastUpdated.value ?: 0L
        db.totalDao().update(Total(ID, current, last))
    }

    private fun formatDate(epochMillis: Long): String {
        val sdf = SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.getDefault())
        return sdf.format(Date(epochMillis))
    }

    companion object { const val ID: Long = 1L }
}
