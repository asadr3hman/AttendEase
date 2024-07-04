package com.example.attendancemanage.ui.auth

import com.google.firebase.Timestamp

data class UserInfo(val name: String = "",
                    val email: String = "",
                    val createdAt: Timestamp = Timestamp.now())