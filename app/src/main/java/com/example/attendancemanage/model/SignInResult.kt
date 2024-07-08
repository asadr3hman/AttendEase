package com.example.attendancemanage.model

import androidx.compose.ui.semantics.Role


data class SignInResult(
    val data: UserData?,
    val errorMessage:String?
)

data class UserData(
    val userId:String,
    val username:String?,
    val userEmail:String?,
    val role: String = "" //e.g. Student or Admin
)
