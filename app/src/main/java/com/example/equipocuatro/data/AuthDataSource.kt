package com.example.equipocuatro.data

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthDataSource @Inject constructor(
    private val auth: FirebaseAuth,
    private val db: FirebaseFirestore
){
    suspend fun registerWithFirebase(email: String, pass: String) =
    auth.createUserWithEmailAndPassword(email,pass).await()

    suspend fun saveUserToFirestore(uid: String, email: String) =
        db.collection("users").document(uid).set(mapOf("email" to email)).await()

    fun getCurrentUsers() = auth.currentUser
}