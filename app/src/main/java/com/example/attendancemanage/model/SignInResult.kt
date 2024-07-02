package com.example.attendancemanage.model


data class SignInResult(
    val data: UserData?,
    val errorMessage:String?
)

data class UserData(
    var userId:String,
    var username:String?,
    var userEmail:String?
)
