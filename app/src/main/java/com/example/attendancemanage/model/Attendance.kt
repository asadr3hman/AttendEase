package com.example.attendancemanage.model

import com.google.firebase.Timestamp

data class Attendance(
    val studentId: String = "",
    val subjectTitle: String = "",
    val date: String = "",
    val status: String = "Absent" // e.g., "Present", "Absent", "Leave"
)