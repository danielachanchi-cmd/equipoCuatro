package com.example.equipocuatro.repository

import com.example.equipocuatro.data.AuthDataSource
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val dataSource: AuthDataSource
){
    suspend fun register(email: String, pass: String): AuthResult? {
        return dataSource.registerWithFirebase(email,pass)
    }

    suspend fun createUserInFirestore(uid: String, email: String) {
        dataSource.saveUserToFirestore(uid, email)
    }

    fun getCurrentUser() = dataSource.getCurrentUsers()

    fun signOut() = dataSource.signOut()

    suspend fun login(email: String, pass: String): AuthResult? {
        return dataSource.loginWithFirebase(email, pass)
    }
}
