package com.example.attendancemanage.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.attendancemanage.model.User
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class UserViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()

    fun addUser(user: User) {
        viewModelScope.launch {
            db.collection("users").document(user.userId).set(user)
                .addOnSuccessListener {
                    // Handle success
                }
                .addOnFailureListener {
                    // Handle failure
                }
        }
    }

    fun getUserData(userId: String, onResult: (User?) -> Unit) {
        viewModelScope.launch {
            val user = getUserDataFromFirestore(userId)
            onResult(user)
        }
    }

    private suspend fun getUserDataFromFirestore(userId: String): User? {
        val userRef = db.collection("users").document(userId)
        val documentSnapshot = userRef.get().await()
        return documentSnapshot.toObject<User>()
    }
}