package com.example.equipocuatro.viewmodel

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.equipocuatro.model.Pokemon
import com.example.equipocuatro.model.Reto
import com.example.equipocuatro.repository.PokemonRepository
import com.example.equipocuatro.repository.RetosRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    // Cambiamos al despachador Unconfined para forzar ejecuciones instantaneas
    private val testDispatcher = UnconfinedTestDispatcher()

    private val application = Mockito.mock(Application::class.java)
    private val retosRepository = Mockito.mock(RetosRepository::class.java)
    private val pokemonRepository = Mockito.mock(PokemonRepository::class.java)

    private lateinit var homeViewModel: HomeViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        Mockito.`when`(application.getString(Mockito.anyInt())).thenReturn("Sin retos disponibles")
        homeViewModel = HomeViewModel(application, retosRepository, pokemonRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun <T> anyKotlin(): T {
        Mockito.any<T>()
        @Suppress("UNCHECKED_CAST")
        return null as T
    }

    @Test
    fun testMetodoToggleMusic() {
        assertTrue(homeViewModel.isMusicEnabled.value == true)
        homeViewModel.toggleMusic()
        assertFalse(homeViewModel.isMusicEnabled.value == true)
    }

    @Test
    fun testMetodoCancelActiveGame() {
        homeViewModel.cancelActiveGame()
        assertFalse(homeViewModel.isSpinning.value == true)
        assertTrue(homeViewModel.showSpinButton.value == true)
        assertNull(homeViewModel.countdownValue.value)
        assertFalse(homeViewModel.pauseBackgroundMusicForGame.value == true)
    }

    @Test
    fun testMetodoOnChallengeDialogClosed() {
        homeViewModel.onChallengeDialogClosed()
        assertNull(homeViewModel.challengeDialog.value)
        assertFalse(homeViewModel.pauseBackgroundMusicForGame.value == true)
    }
}

