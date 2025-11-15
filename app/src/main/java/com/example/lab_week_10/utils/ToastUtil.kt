package com.example.lab_week_10.utils

import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.TextView
import android.widget.Toast
import com.example.lab_week_10.R

object ToastUtil {
    fun showCustomToast(context: Context, line1: String, line2: String? = null) {
        try {
            val inflater = LayoutInflater.from(context)
            val layout = inflater.inflate(R.layout.custom_toast, null)

            val tvMain = layout.findViewById<TextView>(R.id.custom_toast_text)
            val tvSub = layout.findViewById<TextView>(R.id.custom_toast_subtext)

            tvMain.text = line1
            if (line2.isNullOrBlank()) {
                tvSub.text = ""
                tvSub.visibility = android.view.View.GONE
            } else {
                tvSub.text = line2
                tvSub.visibility = android.view.View.VISIBLE
            }

            val toast = Toast(context.applicationContext)
            toast.duration = Toast.LENGTH_LONG
            toast.view = layout
            toast.setGravity(Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL, 0, dpToPx(context, 96))
            toast.show()
        } catch (e: Exception) {
            // fallback
            Toast.makeText(context, line1 + (if (line2 != null) "\n$line2" else ""), Toast.LENGTH_LONG).show()
        }
    }

    private fun dpToPx(context: Context, dp: Int): Int {
        val scale = context.resources.displayMetrics.density
        return (dp * scale + 0.5f).toInt()
    }
}
