package com.example.equipocuatro.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.equipocuatro.model.UserRequest
import com.example.equipocuatro.repository.AuthRepository
import com.example.equipocuatro.utils.Resource
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito

@OptIn(ExperimentalCoroutinesApi::class)
class AuthViewModelTest {

    // Uso esta regla para que LiveData funcione de forma sincrona en los tests
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    // Creo el mock del repositorio con Mockito para simular sus respuestas
    private val authRepository = Mockito.mock(AuthRepository::class.java)
    private lateinit var authViewModel: AuthViewModel

    // Uso este metodo auxiliar para evitar NullPointerException de Mockito con tipos Kotlin no nulos
    private fun <T> anyKotlin(): T {
        Mockito.any<T>()
        @Suppress("UNCHECKED_CAST")
        return null as T
    }

    @Before
    fun setUp() {
        // Reemplazo Dispatchers.Main con el dispatcher de prueba antes de cada test
        Dispatchers.setMain(UnconfinedTestDispatcher())
        authViewModel = AuthViewModel(authRepository)
    }

    @After
    fun tearDown() {
        // Restauro Dispatchers.Main despues de cada test para no afectar otras pruebas
        Dispatchers.resetMain()
    }

    @Test
    fun testMetodoRegisterExitoso() = runTest {
        // Given: configuro los mocks para simular un registro exitoso con uid valido
        val userRequest = UserRequest("test@email.com", "123456")
        val mockAuthResult = Mockito.mock(AuthResult::class.java)
        val mockFirebaseUser = Mockito.mock(FirebaseUser::class.java)

        Mockito.`when`(mockFirebaseUser.uid).thenReturn("uid_de_prueba_123")
        Mockito.`when`(mockAuthResult.user).thenReturn(mockFirebaseUser)
        Mockito.`when`(authRepository.register(anyKotlin())).thenReturn(mockAuthResult)

        // Mockeo createUserInFirestore como funcion suspend que retorna Unit
        Mockito.`when`(authRepository.createUserInFirestore(anyKotlin(), anyKotlin())).thenReturn(Unit)

        // When: llamo al metodo register del ViewModel
        authViewModel.register(userRequest)

        // Then: verifico que el estado final es Resource.Success con el authResult correcto
        val valorFinal = authViewModel.res.value
        assertTrue(valorFinal is Resource.Success)
        assertEquals(mockAuthResult, (valorFinal as Resource.Success).data)
    }

    @Test
    fun testMetodoRegisterError() = runTest {
        // Given: configuro el mock para que lance una excepcion simulando fallo de Firebase
        val userRequest = UserRequest("test@email.com", "123456")
        Mockito.`when`(authRepository.register(anyKotlin())).thenThrow(RuntimeException("Fallo de Firebase"))

        // When: llamo al metodo register del ViewModel
        authViewModel.register(userRequest)

        // Then: verifico que el estado final es Resource.Error con el mensaje correcto
        val valorFinal = authViewModel.res.value
        assertTrue(valorFinal is Resource.Error)
        assertEquals("Error en el registro", (valorFinal as Resource.Error).message)
    }

    @Test
    fun testMetodoLoginExitoso() = runTest {
        // Given: configuro el mock para que devuelva un authResult valido
        val email = "test@email.com"
        val pass = "123456"
        val mockAuthResult = Mockito.mock(AuthResult::class.java)
        Mockito.`when`(authRepository.login(anyKotlin(), anyKotlin())).thenReturn(mockAuthResult)

        // When: llamo al metodo login del ViewModel
        authViewModel.login(email, pass)

        // Then: verifico que el estado final es Resource.Success con el authResult correcto
        val valorFinal = authViewModel.res.value
        assertTrue(valorFinal is Resource.Success)
        assertEquals(mockAuthResult, (valorFinal as Resource.Success).data)
    }

    @Test
    fun testMetodoLoginIncorrecto() = runTest {
        // Given: configuro el mock para que devuelva null simulando credenciales incorrectas
        val email = "test@email.com"
        val pass = "wrong_pass"
        Mockito.`when`(authRepository.login(anyKotlin(), anyKotlin())).thenReturn(null)

        // When: llamo al metodo login del ViewModel
        authViewModel.login(email, pass)

        // Then: verifico que el estado final es Resource.Error con el mensaje correcto
        val valorFinal = authViewModel.res.value
        assertTrue(valorFinal is Resource.Error)
        assertEquals("Login incorrecto", (valorFinal as Resource.Error).message)
    }

    @Test
    fun testMetodoIsUserLoggedInTrue() {
        // Given: configuro el mock para que devuelva un usuario autenticado
        val mockFirebaseUser = Mockito.mock(FirebaseUser::class.java)
        Mockito.`when`(authRepository.getCurrentUser()).thenReturn(mockFirebaseUser)

        // When: llamo a isUserLoggedIn
        val resultado = authViewModel.isUserLoggedIn()

        // Then: verifico que retorna true porque hay un usuario autenticado
        assertTrue(resultado)
    }

    @Test
    fun testMetodoIsUserLoggedInFalse() {
        // Given: configuro el mock para que devuelva null simulando que no hay sesion activa
        Mockito.`when`(authRepository.getCurrentUser()).thenReturn(null)

        // When: llamo a isUserLoggedIn
        val resultado = authViewModel.isUserLoggedIn()

        // Then: verifico que retorna false porque no hay usuario autenticado
        assertFalse(resultado)
    }

    @Test
    fun testMetodoSignOut() {
        // Given: no necesito configuracion previa porque signOut no retorna nada

        // When: llamo a signOut del ViewModel
        authViewModel.signOut()

        // Then: verifico con Mockito que el repositorio recibio la orden de cerrar sesion
        Mockito.verify(authRepository).signOut()
    }
}