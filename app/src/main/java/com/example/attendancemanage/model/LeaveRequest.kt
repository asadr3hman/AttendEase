package com.example.attendancemanage.model

data class LeaveRequest(
    val studentId: String = "",
    val subjectTitle: String = "",
    val date: String = "",
    val status: String = "Pending" // "Pending", "Approved", "Rejected"
)