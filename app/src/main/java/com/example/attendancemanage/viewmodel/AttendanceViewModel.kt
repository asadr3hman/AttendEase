package com.example.attendancemanage.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.attendancemanage.model.Attendance
import com.example.attendancemanage.model.LeaveRequest
import com.example.attendancemanage.model.Subject
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import com.google.firebase.firestore.toObjects
import kotlinx.coroutines.tasks.await

class AttendanceViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()

    suspend fun markAttendance(attendance: Attendance) {
        try {
            db.collection("attendance").add(attendance.copy()).await()
        } catch (e: Exception) {
            Log.e("AttendanceViewModel", "Error adding attendance record", e)
        }
    }
    // Fetch subject data
    suspend fun getSubjectData(subjectId: String): Subject? {
        return try {
            val subjectRef = db.collection("subjects").document(subjectId)
            val documentSnapshot = subjectRef.get().await()
            documentSnapshot.toObject<Subject>()
        } catch (e: Exception) {
            Log.e("AttendanceViewModel", "Error fetching subject data", e)
            null
        }
    }


    // Request leave
    suspend fun requestLeave(leaveRequest: LeaveRequest) {
        try {
            db.collection("leaveRequests").document(leaveRequest.leaveRequestId).set(leaveRequest).await()
        } catch (e: Exception) {
            Log.e("AttendanceViewModel", "Error requesting leave", e)
        }
    }

    // Fetch attendance for a subject
    suspend fun getAttendanceForSubject(subjectId: String): List<Attendance> {
        return try {
            val attendanceRef = db.collection("attendance").whereEqualTo("subjectId", subjectId)
            val querySnapshot = attendanceRef.get().await()
            querySnapshot.toObjects<Attendance>()
        } catch (e: Exception) {
            Log.e("AttendanceViewModel", "Error fetching attendance records", e)
            emptyList()
        }
    }
}