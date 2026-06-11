package com.example.equipocuatro.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashViewModel : ViewModel() {

    private val _navigateToLogin = MutableLiveData<Boolean>()
    val navigateToLogin: LiveData<Boolean> get() = _navigateToLogin

    init {
        startTimer()
    }

    private fun startTimer() {
        viewModelScope.launch {
            delay(5000) // 5 segundos
            _navigateToLogin.value = true
        }
    }
}