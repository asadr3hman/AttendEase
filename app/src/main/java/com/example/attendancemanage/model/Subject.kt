package com.example.attendancemanage.model

import kotlinx.coroutines.internal.OpDescriptor
data class Subject(
    val subjectTitle: String = "",
    val name: String = "",
    val students: List<String> = emptyList() // List of student UIDs
)