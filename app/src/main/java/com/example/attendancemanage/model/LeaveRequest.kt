package com.example.attendancemanage.model

import com.google.firebase.Timestamp

data class LeaveRequest(
    val studentId: String = "",
    val subjectTitle: String = "",
    val date: String = "",
    val status: String = "Pending" // "Pending", "Approved", "Rejected"
)