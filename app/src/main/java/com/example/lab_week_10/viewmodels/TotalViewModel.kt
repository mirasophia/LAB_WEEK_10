package com.example.lab_week_10.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class TotalViewModel : ViewModel() {

    // Backing property
    private val _total = MutableLiveData<Int>()
    val total: LiveData<Int> = _total

    init {
        // initialize to 0
        _total.postValue(0)
    }

    fun incrementTotal() {
        // use postValue to be safe for background threads; setValue would also work on main thread
        _total.postValue((_total.value ?: 0) + 1)
    }

    // optional setter (useful later for Room)
    fun setTotal(newTotal: Int) {
        _total.postValue(newTotal)
    }
}
