package com.example.attendancemanage.model

data class AttendanceReport(
    val totalClasses: Int,
    val attendedClasses: Int,
    val absentClasses: Int,
    val leavesClasses: Int
)
