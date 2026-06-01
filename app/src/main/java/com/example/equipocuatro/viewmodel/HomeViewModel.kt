package com.example.equipocuatro.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.equipocuatro.model.ChallengeDialogData
import com.example.equipocuatro.repository.PokemonRepository
import com.example.equipocuatro.repository.RetosRepository
import coil.imageLoader
import coil.request.ImageRequest
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    companion object {
        const val SPIN_DURATION_MS = 4000L
        const val COUNTDOWN_INTERVAL_MS = 1000L
        const val COUNTDOWN_ZERO_HOLD_MS = 800L
    }

    private val retosRepository = RetosRepository(application)
    private val pokemonRepository = PokemonRepository()

    private val _isMusicEnabled = MutableLiveData(true)
    val isMusicEnabled: LiveData<Boolean> = _isMusicEnabled

    private val _showSpinButton = MutableLiveData(true)
    val showSpinButton: LiveData<Boolean> = _showSpinButton

    private val _countdownValue = MutableLiveData<Int?>(null)
    val countdownValue: LiveData<Int?> = _countdownValue

    private val _bottleRotation = MutableLiveData(0f)
    val bottleRotation: LiveData<Float> = _bottleRotation

    private val _spinDegrees = MutableLiveData(0f)
    val spinDegrees: LiveData<Float> = _spinDegrees

    private val _isSpinning = MutableLiveData(false)
    val isSpinning: LiveData<Boolean> = _isSpinning

    private val _pauseBackgroundMusicForGame = MutableLiveData(false)
    val pauseBackgroundMusicForGame: LiveData<Boolean> = _pauseBackgroundMusicForGame

    private val _challengeDialog = MutableLiveData<ChallengeDialogData?>()
    val challengeDialog: LiveData<ChallengeDialogData?> = _challengeDialog

    private var currentRotation = 0f
    private var isGameInProgress = false
    private var gameJob: Job? = null

    fun toggleMusic() {
        if (isGameInProgress) return
        _isMusicEnabled.value = !(_isMusicEnabled.value ?: true)
    }

    fun cancelActiveGame() {
        gameJob?.cancel()
        gameJob = null
        if (!isGameInProgress) {
            _spinDegrees.value = 0f
            _isSpinning.value = false
            return
        }
        isGameInProgress = false
        _isSpinning.value = false
        _showSpinButton.value = true
        _countdownValue.value = null
        _spinDegrees.value = 0f
        _pauseBackgroundMusicForGame.value = false
    }

    fun startBottleSpin() {
        if (isGameInProgress) return

        isGameInProgress = true
        _showSpinButton.value = false
        _countdownValue.value = null
        _isSpinning.value = true

        if (_isMusicEnabled.value == true) {
            _pauseBackgroundMusicForGame.value = true
        }

        val spinAmount = Random.nextInt(4, 7) * 360f + Random.nextInt(0, 360)
        _spinDegrees.value = 0f
        _spinDegrees.value = spinAmount
        currentRotation += spinAmount
        _bottleRotation.value = currentRotation

        gameJob?.cancel()
        gameJob = viewModelScope.launch {
            val retoDeferred = async { retosRepository.getRandomReto() }
            val pokemonDeferred = async { pokemonRepository.getRandomPokemon() }

            delay(SPIN_DURATION_MS)
            _isSpinning.value = false
            _spinDegrees.value = 0f

            val reto = retoDeferred.await()
            val pokemon = pokemonDeferred.await()
            val description = reto?.descripcion
                ?: getApplication<Application>().getString(
                    com.example.equipocuatro.R.string.sin_retos_disponibles
                )

            getApplication<Application>().imageLoader.enqueue(
                ImageRequest.Builder(getApplication())
                    .data(pokemon.img)
                    .build()
            )

            for (i in 3 downTo 0) {
                _countdownValue.value = i
                delay(COUNTDOWN_INTERVAL_MS)
            }

            delay(COUNTDOWN_ZERO_HOLD_MS)
            _showSpinButton.value = true

            _challengeDialog.value = ChallengeDialogData(description, pokemon.img)
            _countdownValue.value = null
            gameJob = null
        }
    }

    fun onChallengeDialogClosed() {
        _challengeDialog.value = null
        isGameInProgress = false
        _pauseBackgroundMusicForGame.value = false
    }
}
