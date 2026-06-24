package com.example.equipocuatro.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SplashViewModelTest {

    // Uso esta regla para que LiveData funcione de forma sincrona en los tests
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    // Creo un dispatcher de prueba para controlar el tiempo virtual con precision
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        // Reemplazo Dispatchers.Main con el dispatcher de prueba antes de cada test
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        // Restauro Dispatchers.Main despues de cada test para no afectar otras pruebas
        Dispatchers.resetMain()
    }

    @Test
    fun whenViewModelStarts_navigateToLoginIsNullBefore5Seconds() =
        runTest(testDispatcher) {
            // Given: creo el ViewModel, toma testDispatcher automaticamente via Dispatchers.Main
            val viewModel = SplashViewModel()

            // When: avanzo el tiempo virtual 4999ms, un milisegundo antes de que el delay termine
            advanceTimeBy(4999)
            runCurrent()

            // Then: verifico que navigateToLogin es nulo porque el delay de 5000ms no termino
            assertNull(viewModel.navigateToLogin.value)
        }

    @Test
    fun whenExactly5SecondsPass_navigateToLoginIsTrue() =
        runTest(testDispatcher) {
            // Given: creo el ViewModel, toma testDispatcher automaticamente via Dispatchers.Main
            val viewModel = SplashViewModel()

            // When: avanzo exactamente 5000ms, advanceTimeBy es inclusivo en el limite exacto
            advanceTimeBy(5000)
            runCurrent()

            // Then: verifico que navigateToLogin es true porque delay(5000) se completo en 5000ms exactos
            assertTrue(viewModel.navigateToLogin.value == true)
        }

    @Test
    fun whenMoreThan5SecondsPass_navigateToLoginIsTrue() =
        runTest(testDispatcher) {
            // Given: creo el ViewModel, toma testDispatcher automaticamente via Dispatchers.Main
            val viewModel = SplashViewModel()

            // When: avanzo 5001ms para superar el delay de 5 segundos
            advanceTimeBy(5001)
            runCurrent()

            // Then: verifico que navigateToLogin es true porque el delay ya termino
            assertTrue(viewModel.navigateToLogin.value == true)
        }
}