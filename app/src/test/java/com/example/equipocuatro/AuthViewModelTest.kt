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

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val authRepository = Mockito.mock(AuthRepository::class.java)
    private lateinit var authViewModel: AuthViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
        authViewModel = AuthViewModel(authRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    // El truco maestro para evitar los nulos molestos de Mockito en Kotlin
    private fun <T> anyKotlin(): T {
        Mockito.any<T>()
        @Suppress("UNCHECKED_CAST")
        return null as T
    }

    @Test
    fun testMetodoRegister_Exitoso() = runTest {
        ////given
        val userRequest = UserRequest("test@email.com", "123456")

        // Creamos mocks encadenados para simular authResult.user.uid
        val mockAuthResult = Mockito.mock(AuthResult::class.java)
        val mockFirebaseUser = Mockito.mock(FirebaseUser::class.java)

        Mockito.`when`(mockFirebaseUser.uid).thenReturn("uid_de_prueba_123")
        Mockito.`when`(mockAuthResult.user).thenReturn(mockFirebaseUser)

        // Configuramos las respuestas esperadas del repositorio
        Mockito.`when`(authRepository.register(anyKotlin())).thenReturn(mockAuthResult)

        ////when
        authViewModel.register(userRequest)

        ////Then
        // Verificamos que el estado final sea un Success que envuelva nuestro mockAuthResult
        val valorFinal = authViewModel.res.value
        assertTrue(valorFinal is Resource.Success)
        assertEquals(mockAuthResult, (valorFinal as Resource.Success).data)
    }

    @Test
    fun testMetodoRegister_Error() = runTest {
        ////given
        val userRequest = UserRequest("test@email.com", "123456")

        // Forzamos a que lance una excepción simulando un fallo de red o credenciales duplicadas
        Mockito.`when`(authRepository.register(anyKotlin())).thenThrow(RuntimeException("Fallo de Firebase"))

        ////when
        authViewModel.register(userRequest)

        ////Then
        val valorFinal = authViewModel.res.value
        assertTrue(valorFinal is Resource.Error)
        assertEquals("Error en el registro", (valorFinal as Resource.Error).message)
    }

    @Test
    fun testMetodoLogin_Exitoso() = runTest {
        ////given
        val email = "test@email.com"
        val pass = "123456"
        val mockAuthResult = Mockito.mock(AuthResult::class.java)

        Mockito.`when`(authRepository.login(anyKotlin(), anyKotlin())).thenReturn(mockAuthResult)

        ////when
        authViewModel.login(email, pass)

        ////Then
        val valorFinal = authViewModel.res.value
        assertTrue(valorFinal is Resource.Success)
        assertEquals(mockAuthResult, (valorFinal as Resource.Success).data)
    }

    @Test
    fun testMetodoLogin_Incorrecto() = runTest {
        ////given
        val email = "test@email.com"
        val pass = "wrong_pass"

        // Simulamos que las credenciales devuelven un resultado nulo
        Mockito.`when`(authRepository.login(anyKotlin(), anyKotlin())).thenReturn(null)

        ////when
        authViewModel.login(email, pass)

        ////Then
        val valorFinal = authViewModel.res.value
        assertTrue(valorFinal is Resource.Error)
        assertEquals("Login incorrecto", (valorFinal as Resource.Error).message)
    }

    @Test
    fun testMetodoIsUserLoggedIn_True() {
        ////given
        val mockFirebaseUser = Mockito.mock(FirebaseUser::class.java)
        Mockito.`when`(authRepository.getCurrentUser()).thenReturn(mockFirebaseUser)

        ////when
        val resultado = authViewModel.isUserLoggedIn()

        ////Then
        assertTrue(resultado)
    }

    @Test
    fun testMetodoIsUserLoggedIn_False() {
        ////given
        Mockito.`when`(authRepository.getCurrentUser()).thenReturn(null)

        ////when
        val resultado = authViewModel.isUserLoggedIn()

        ////Then
        assertFalse(resultado)
    }

    @Test
    fun testMetodoSignOut() {
        ////given (Nada inicial porque signOut no retorna nada)

        ////when
        authViewModel.signOut()

        ////Then
        // Verificamos de forma limpia que el repositorio recibió la orden exacta de cerrar sesión
        Mockito.verify(authRepository).signOut()
    }
}