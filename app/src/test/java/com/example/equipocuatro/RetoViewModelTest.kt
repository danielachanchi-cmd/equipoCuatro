package com.example.equipocuatro.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.equipocuatro.model.Reto
import com.example.equipocuatro.repository.RetosRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito
import org.mockito.invocation.InvocationOnMock
import org.mockito.stubbing.Answer

@OptIn(ExperimentalCoroutinesApi::class)
class RetoViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val retoRepository = Mockito.mock(RetosRepository::class.java)
    private lateinit var retoViewModel: RetoViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
        retoViewModel = RetoViewModel(retoRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    // Método auxiliar para crear objetos de prueba rápidos
    private fun crearRetoEjemplo(): Reto {
        return Reto(1, "Descripción de prueba")
    }

    // El truco definitivo para evitar el NullPointerException en Kotlin
    private fun <T> anyKotlin(): T {
        Mockito.any<T>()
        @Suppress("UNCHECKED_CAST")
        return null as T
    }

    @Test
    fun testMetodoGetListReto() = runTest {
        ////given
        val mockRetos = mutableListOf(crearRetoEjemplo())
        val expectedResult = mockRetos

        Mockito.`when`(retoRepository.getListReto()).thenReturn(mockRetos)

        ////when
        retoViewModel.getListReto()

        ////Then
        assertEquals(expectedResult, retoViewModel.listReto.value)
    }

    @Test
    fun testMetodoUpdateReto() = runTest {
        ////given
        val retoTest = crearRetoEjemplo()
        val mockRetos = mutableListOf(retoTest)

        val expectedResult = "Reto actualizado correctamente"
        val expectedSuccess = true
        val expectedList = mockRetos

        Mockito.`when`(retoRepository.updateReto(anyKotlin())).thenReturn(Result.success(Unit))
        Mockito.`when`(retoRepository.getListReto()).thenReturn(mockRetos)

        var resulMessage = ""
        var resulSuccess = false

        ////when
        retoViewModel.updateReto(retoTest) { msg, success ->
            resulMessage = msg
            resulSuccess = success
        }

        ////Then
        assertEquals(expectedResult, resulMessage)
        assertEquals(expectedSuccess, resulSuccess)
        assertEquals(expectedList, retoViewModel.listReto.value)
    }

    @Test
    fun testMetodoSaveReto() = runTest {
        ////given
        val nuevoReto = crearRetoEjemplo()
        val mockRetos = mutableListOf(nuevoReto)

        val expectedResult = "Reto guardado"
        val expectedSuccess = true
        val expectedList = mockRetos

        Mockito.doAnswer(object : Answer<Unit> {
            override fun answer(invocation: InvocationOnMock) {
                val callback = invocation.getArgument<(String, Boolean) -> Unit>(1)
                callback.invoke("Reto guardado", true)
            }
        }).`when`(retoRepository).saveReto(anyKotlin(), anyKotlin())

        Mockito.`when`(retoRepository.getListReto()).thenReturn(mockRetos)

        var resulMessage = ""
        var resulSuccess = false

        ////when
        retoViewModel.saveReto(nuevoReto) { msg, success ->
            resulMessage = msg
            resulSuccess = success
        }

        ////Then
        assertEquals(expectedResult, resulMessage)
        assertEquals(expectedSuccess, resulSuccess)
        assertEquals(expectedList, retoViewModel.listReto.value)
    }

    @Test
    fun testMetodoDeleteReto() = runTest {
        ////given
        val retoAEliminar = crearRetoEjemplo()
        val mockRetosVacios = mutableListOf<Reto>()

        val expectedResult = "Reto eliminado correctamente"
        val expectedSuccess = true
        val expectedList = mockRetosVacios

        Mockito.`when`(retoRepository.deleteReto(anyKotlin())).thenReturn(Result.success(Unit))
        Mockito.`when`(retoRepository.getListReto()).thenReturn(mockRetosVacios)

        var resulMessage = ""
        var resulSuccess = false

        ////when
        retoViewModel.deleteReto(retoAEliminar) { msg, success ->
            resulMessage = msg
            resulSuccess = success
        }

        ////Then
        assertEquals(expectedResult, resulMessage)
        assertEquals(expectedSuccess, resulSuccess)
        assertEquals(expectedList, retoViewModel.listReto.value)
    }
}