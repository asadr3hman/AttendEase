package com.example.attendancemanage.viewmodel

import androidx.lifecycle.ViewModel
import com.example.attendancemanage.model.Attendance
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObjects
import kotlinx.coroutines.tasks.await

class AttendanceViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()

    fun markAttendance(attendance: Attendance) {
        val attendanceRef = db.collection("attendance")
            .document("${attendance.studentId}_${attendance.subjectTitle}_${attendance.date}")

        attendanceRef.set(attendance).addOnSuccessListener { /* Handle success */ }
            .addOnFailureListener { /* Handle failure */ }
    }

    suspend fun getAttendanceForSubject(studentId: String, subjectTitle: String): List<Attendance> {
        val result = db.collection("attendance").whereEqualTo("studentId", studentId)
            .whereEqualTo("subjectTitle", subjectTitle).get().await()

        return result.toObjects()
    }
}