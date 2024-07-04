package com.example.attendancemanage.model



data class Student(
    val email: String = "",
    val rollNo: String = "",
    val name: String = "",
    var subjects: List<Subject>? = null
)
