package com.example.attendancemanage.model

data class User(
    val userId: String = "",
    val email: String = "",
    val name: String = "",
    val role: String = "", // "Student" or "Admin"
    val rollNo: String? = null,
    val subjects: List<Subject>? = null
)