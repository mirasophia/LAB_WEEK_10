package com.example.lab_week_10

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.lab_week_10.utils.ToastUtil
import com.example.lab_week_10.viewmodels.TotalViewModel

class FirstFragment : Fragment() {

    private lateinit var viewModel: TotalViewModel
    private var textTotal: TextView? = null
    private var textUpdated: TextView? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_first, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        textTotal = view.findViewById(R.id.text_total_fragment)
        textUpdated = view.findViewById(R.id.text_updated_fragment)

        viewModel = ViewModelProvider(requireActivity())[TotalViewModel::class.java]

        viewModel.total.observe(viewLifecycleOwner) { total ->
            textTotal?.text = getString(R.string.text_total, total)
        }

        viewModel.lastUpdated.observe(viewLifecycleOwner) { epoch ->
            if (epoch <= 0L) {
                textUpdated?.text = getString(R.string.text_updated_never)
            } else {
                textUpdated?.text = getString(R.string.text_updated_at, formatDate(epoch))
            }
        }
    }

    private fun formatDate(epochMillis: Long): String {
        val sdf = java.text.SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", java.util.Locale.getDefault())
        return sdf.format(java.util.Date(epochMillis))
    }
}
