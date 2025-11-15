package com.example.lab_week_10.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class TotalViewModel : ViewModel() {
    private val _total = MutableLiveData<Int>()
    val total: LiveData<Int> = _total

    private val _lastUpdated = MutableLiveData<Long>()
    val lastUpdated: LiveData<Long> = _lastUpdated

    init {
        _total.postValue(0)
        _lastUpdated.postValue(0L)
    }

    fun incrementTotal() {
        _total.postValue((_total.value ?: 0) + 1)
    }

    fun setTotal(newTotal: Int) { _total.postValue(newTotal) }
    fun setLastUpdated(epochMillis: Long) { _lastUpdated.postValue(epochMillis) }
}
