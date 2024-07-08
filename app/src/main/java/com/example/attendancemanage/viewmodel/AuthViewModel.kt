package com.example.attendancemanage.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.attendancemanage.model.SignInResult
import com.example.attendancemanage.model.UserData
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class AuthViewModel : ViewModel() {

    private var _signinResult = MutableLiveData<SignInResult>()
    val signinResult: LiveData<SignInResult> get() = _signinResult

    private val firestoreDB = FirebaseFirestore.getInstance()
    private val auth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }

    init {
        // Check the current authentication state when the ViewModel is created
        viewModelScope.launch {
            auth.currentUser?.let { user ->
                val username = getUserUsernameFromFirestore(user.email!!)
                _signinResult.value = SignInResult(
                    data = UserData(
                        userId = user.uid,
                        username = username,
                        userEmail = user.email
                    ),
                    errorMessage = null
                )
            }
        }
    }

    suspend fun signUpUser(
        username: String,
        email: String,
        password: String,
        callback: (Boolean,String?, Exception?) -> Unit
    ) {
        try {
            val authResult = auth.createUserWithEmailAndPassword(email, password).await()
            authResult.user?.let { user ->
                val userData = UserData(user.uid, username, email, "Student")
                saveUserToFireStore(user.uid, userData)
                callback(true,user.uid, null)
            } ?: run {
                callback(false, null, Exception("Failed to get user ID after sign up"))
            }
        } catch (e: Exception) {
            Log.e("SignUp", "Failed to sign up user", e)
            callback(false, null ,e)
        }
    }

    suspend fun signInWithEmailPassword(email: String, password: String): SignInResult {
        return try {
            val userCredential = auth.signInWithEmailAndPassword(email, password).await()
            val user = userCredential.user
            val username = getUserUsernameFromFirestore(email)
            val result = SignInResult(
                data = user?.let {
                    UserData(
                        userId = it.uid,
                        username = username,
                        userEmail = it.email
                    )
                },
                errorMessage = null
            )
            _signinResult.value = result
            return result
        } catch (e: Exception) {
            e.printStackTrace()
            SignInResult(data = null, errorMessage = e.message)
        }
    }

    private fun saveUserToFireStore(userId: String, userInfo: UserData) {
        val userDocRef = firestoreDB.collection("users").document(userId)
        userDocRef.set(userInfo)
            .addOnSuccessListener {
                Log.v("Dataa", "Data saved successfully")
            }
            .addOnFailureListener { e ->
                // Handle error
                Log.v("Dataa", "Data don't saved ${e.message}")
            }
    }

    private suspend fun getUserUsernameFromFirestore(email: String): String? {
        val usersCollection = FirebaseFirestore.getInstance().collection("users")
        val querySnapshot = usersCollection.whereEqualTo("userEmail", email).get().await()
        return if (!querySnapshot.isEmpty) {
            querySnapshot.documents[0].getString("username")
        } else {
            null
        }
    }

    suspend fun getSignedInUser(): UserData? {
        return auth.currentUser?.run {
            var username = displayName ?: getUserUsernameFromFirestore(email!!)
            if (displayName == null || displayName.toString() == "") {
                username = getUserUsernameFromFirestore(email!!)
            }

            UserData(
                userId = uid,
                username = username,
                userEmail = email,
            )
        }
    }

    suspend fun getUserRole(userId: String): String? {
        val doc = firestoreDB.collection("users").document(userId).get().await()
        return if (doc.exists()) {
            doc.getString("role")
        } else {
            null
        }
    }
}