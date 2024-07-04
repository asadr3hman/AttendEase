package com.example.attendancemanage.model

data class Attendance(
    val studentId: String = "",
    val subjectTitle: String = "",
    val date: String = "",
    val status: String = "" // e.g., "Present", "Absent", "Leave"
)