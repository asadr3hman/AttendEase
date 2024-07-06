package com.example.attendancemanage.model

import com.google.firebase.Timestamp

data class LeaveRequest(
    val leaveRequestId: String,
    val studentId: String,
    val subjectId: String,
    val startDate: Timestamp,
    val endDate: Timestamp,
    val reason: String = "",
    val status: String = "Pending" // "Pending", "Approved", "Rejected"
)