package com.example.equipocuatro.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SplashViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    // Usamos StandardTestDispatcher para poder pausar y adelantar el tiempo virtual de forma precisa
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun cuandoIniciaElViewModel_navigateToLoginEsNuloAntesDeLos5Segundos() = runTest(testDispatcher) {
        ////given
        val viewModel = SplashViewModel()

        ////when
        // Avanzamos solo 2 segundos (el delay de 5 segundos sigue pendiente)
        testDispatcher.scheduler.advanceTimeBy(2000)

        ////Then
        assertNull(viewModel.navigateToLogin.value)
    }
}