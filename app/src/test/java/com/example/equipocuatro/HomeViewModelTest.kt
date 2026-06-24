package com.example.equipocuatro.viewmodel

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.equipocuatro.repository.PokemonRepository
import com.example.equipocuatro.repository.RetosRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
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

    // Uso esta regla para que LiveData funcione de forma sincrona en los tests
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    // Creo el dispatcher para ejecutar corrutinas de forma inmediata en los tests
    private val testDispatcher = UnconfinedTestDispatcher()

    // Creo los mocks de las dependencias del ViewModel
    private val application = Mockito.mock(Application::class.java)
    private val retosRepository = Mockito.mock(RetosRepository::class.java)
    private val pokemonRepository = Mockito.mock(PokemonRepository::class.java)

    private lateinit var homeViewModel: HomeViewModel

    @Before
    fun setUp() {
        // Reemplazo Dispatchers.Main con el dispatcher de prueba antes de cada test
        Dispatchers.setMain(testDispatcher)
        Mockito.`when`(application.getString(Mockito.anyInt())).thenReturn("Sin retos disponibles")
        Mockito.`when`(application.applicationContext).thenReturn(application)

        // Creo el ViewModel con los mocks de las dependencias
        homeViewModel = HomeViewModel(application, retosRepository, pokemonRepository)
    }

    @After
    fun tearDown() {
        // Restauro Dispatchers.Main despues de cada test para no afectar otras pruebas
        Dispatchers.resetMain()
    }

    @Test
    fun testEstadoInicialDelViewModel() {
        // Given: el ViewModel recien fue creado en setUp

        // When: no ejecuto ninguna accion

        // Then: verifico que todos los valores iniciales son los correctos
        assertTrue(homeViewModel.isMusicEnabled.value == true)
        assertTrue(homeViewModel.showSpinButton.value == true)
        assertNull(homeViewModel.countdownValue.value)
        assertFalse(homeViewModel.isSpinning.value == true)
        assertFalse(homeViewModel.pauseBackgroundMusicForGame.value == true)
        assertNull(homeViewModel.challengeDialog.value)
    }

    @Test
    fun testMetodoToggleMusicHabilitaYDeshabilita() {
        // Given: la musica esta habilitada por defecto al iniciar el ViewModel
        assertTrue(homeViewModel.isMusicEnabled.value == true)

        // When: llamo a toggleMusic para deshabilitar la musica
        homeViewModel.toggleMusic()

        // Then: verifico que la musica se deshabilito correctamente
        assertFalse(homeViewModel.isMusicEnabled.value == true)

        // When: llamo a toggleMusic de nuevo para habilitarla
        homeViewModel.toggleMusic()

        // Then: verifico que la musica se habilito nuevamente
        assertTrue(homeViewModel.isMusicEnabled.value == true)
    }

    @Test
    fun testMetodoCancelActiveGameSinJuegoEnProgreso() {
        // Given: no hay juego en progreso al iniciar

        // When: llamo a cancelActiveGame sin haber iniciado un juego
        homeViewModel.cancelActiveGame()

        // Then: verifico que todos los estados son los correctos tras cancelar
        assertFalse(homeViewModel.isSpinning.value == true)
        assertTrue(homeViewModel.showSpinButton.value == true)
        assertNull(homeViewModel.countdownValue.value)
        assertFalse(homeViewModel.pauseBackgroundMusicForGame.value == true)
    }

    @Test
    fun testMetodoOnChallengeDialogClosedLimpiaEstado() {
        // Given: simulo que hay un dialogo abierto

        // When: cierro el dialogo de desafio
        homeViewModel.onChallengeDialogClosed()

        // Then: verifico que el dialogo es nulo y la musica de fondo se restauro
        assertNull(homeViewModel.challengeDialog.value)
        assertFalse(homeViewModel.pauseBackgroundMusicForGame.value == true)
    }
}