package com.example.attendancemanage.viewmodel

import androidx.lifecycle.ViewModel
import com.example.attendancemanage.model.SignInResult
import com.example.attendancemanage.model.UserData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class AuthViewModel : ViewModel(){

    private val auth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }

    suspend fun signInWithEmailPassword(email: String, password: String): SignInResult {
        return try {
            val userCredential = auth.signInWithEmailAndPassword(email, password).await()
            val user = userCredential.user
            val username = getUserUsernameFromFirestore(email)
            SignInResult(
                data = user?.let {
                    UserData(
                        userId = it.uid,
                        username = username,
                        userEmail = it.email
                    )
                },
                errorMessage = null
            )
        } catch (e: Exception) {
            e.printStackTrace()
            SignInResult(data = null, errorMessage = e.message)
        }
    }


    private suspend fun getUserUsernameFromFirestore(email: String): String? {
        val usersCollection = FirebaseFirestore.getInstance().collection("users")
        val querySnapshot = usersCollection.whereEqualTo("email", email).get().await()
        return if (!querySnapshot.isEmpty) {
            querySnapshot.documents[0].getString("name")
        } else {
            null
        }
    }

}