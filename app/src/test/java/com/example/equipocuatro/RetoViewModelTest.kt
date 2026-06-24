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

    // Uso esta regla para que LiveData funcione de forma sincrona en los tests
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    // Creo el mock del repositorio con Mockito para simular sus respuestas
    private val retoRepository = Mockito.mock(RetosRepository::class.java)
    private lateinit var retoViewModel: RetoViewModel

    @Before
    fun setUp() {
        // Reemplazo Dispatchers.Main con UnconfinedTestDispatcher para ejecutar corrutinas inmediatamente
        Dispatchers.setMain(UnconfinedTestDispatcher())
        retoViewModel = RetoViewModel(retoRepository)
    }

    @After
    fun tearDown() {
        // Restauro Dispatchers.Main despues de cada test para no afectar otras pruebas
        Dispatchers.resetMain()
    }

    // Creo un objeto Reto de prueba para reutilizar en los tests
    private fun crearRetoEjemplo(): Reto {
        return Reto(1, "Descripcion de prueba")
    }

    // Uso este metodo auxiliar para evitar NullPointerException de Mockito con tipos Kotlin no nulos
    private fun <T> anyKotlin(): T {
        Mockito.any<T>()
        @Suppress("UNCHECKED_CAST")
        return null as T
    }

    @Test
    fun testMetodoGetListReto() = runTest {
        // Given: preparo el mock del repositorio para devolver una lista con un reto
        val mockRetos = mutableListOf(crearRetoEjemplo())
        Mockito.`when`(retoRepository.getListReto()).thenReturn(mockRetos)

        // When: llamo al metodo getListReto del ViewModel
        retoViewModel.getListReto()

        // Then: verifico que listReto contiene la lista devuelta por el repositorio
        assertEquals(mockRetos, retoViewModel.listReto.value)
    }

    @Test
    fun testMetodoUpdateReto() = runTest {
        // Given: preparo el mock para que updateReto sea exitoso y getListReto devuelva la lista
        val retoTest = crearRetoEjemplo()
        val mockRetos = mutableListOf(retoTest)

        Mockito.`when`(retoRepository.updateReto(anyKotlin())).thenReturn(Result.success(Unit))
        Mockito.`when`(retoRepository.getListReto()).thenReturn(mockRetos)

        var resultMessage = ""
        var resultSuccess = false

        // When: llamo al metodo updateReto del ViewModel
        retoViewModel.updateReto(retoTest) { msg, success ->
            resultMessage = msg
            resultSuccess = success
        }

        // Then: verifico el mensaje, el exito y que la lista se actualizo correctamente
        assertEquals("Reto actualizado correctamente", resultMessage)
        assertEquals(true, resultSuccess)
        assertEquals(mockRetos, retoViewModel.listReto.value)
    }

    @Test
    fun testMetodoSaveReto() = runTest {
        // Given: preparo el mock para que saveReto llame al callback con exito
        val nuevoReto = crearRetoEjemplo()
        val mockRetos = mutableListOf(nuevoReto)

        Mockito.doAnswer(object : Answer<Unit> {
            override fun answer(invocation: InvocationOnMock) {
                // Obtengo el callback del segundo argumento y lo invoco con exito
                val callback = invocation.getArgument<(String, Boolean) -> Unit>(1)
                callback.invoke("Reto guardado", true)
            }
        }).`when`(retoRepository).saveReto(anyKotlin(), anyKotlin())

        Mockito.`when`(retoRepository.getListReto()).thenReturn(mockRetos)

        var resultMessage = ""
        var resultSuccess = false

        // When: llamo al metodo saveReto del ViewModel
        retoViewModel.saveReto(nuevoReto) { msg, success ->
            resultMessage = msg
            resultSuccess = success
        }

        // Then: verifico el mensaje, el exito y que la lista se actualizo correctamente
        assertEquals("Reto guardado", resultMessage)
        assertEquals(true, resultSuccess)
        assertEquals(mockRetos, retoViewModel.listReto.value)
    }

    @Test
    fun testMetodoDeleteReto() = runTest {
        // Given: preparo el mock para que deleteReto sea exitoso y la lista quede vacia
        val retoAEliminar = crearRetoEjemplo()
        val mockRetosVacios = mutableListOf<Reto>()

        Mockito.`when`(retoRepository.deleteReto(anyKotlin())).thenReturn(Result.success(Unit))
        Mockito.`when`(retoRepository.getListReto()).thenReturn(mockRetosVacios)

        var resultMessage = ""
        var resultSuccess = false

        // When: llamo al metodo deleteReto del ViewModel
        retoViewModel.deleteReto(retoAEliminar) { msg, success ->
            resultMessage = msg
            resultSuccess = success
        }

        // Then: verifico el mensaje, el exito y que la lista quedo vacia
        assertEquals("Reto eliminado correctamente", resultMessage)
        assertEquals(true, resultSuccess)
        assertEquals(mockRetosVacios, retoViewModel.listReto.value)
    }
}