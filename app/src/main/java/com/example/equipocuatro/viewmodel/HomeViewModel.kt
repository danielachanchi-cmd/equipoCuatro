package com.example.equipocuatro.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HomeViewModel : ViewModel() {

    private val _isMusicEnabled = MutableLiveData<Boolean>(true)
    val isMusicEnabled: LiveData<Boolean> get() = _isMusicEnabled

    fun toggleMusic() {
        _isMusicEnabled.value = !(_isMusicEnabled.value ?: true)
    }
}