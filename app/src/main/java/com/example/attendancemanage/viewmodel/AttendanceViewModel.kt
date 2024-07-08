package com.example.attendancemanage.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.attendancemanage.model.Attendance
import com.example.attendancemanage.model.LeaveRequest
import com.example.attendancemanage.model.Subject
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import com.google.firebase.firestore.toObjects
import com.ozcanalasalvar.datepicker.model.Date
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
            db.collection("leaveRequests").add(leaveRequest.copy()).await()
            db.collection("attendance").add(leaveRequest.copy()).await()
        } catch (e: Exception) {
            Log.e("AttendanceViewModel", "Error requesting leave", e)
        }
    }

    // Fetch attendance for a subject
    suspend fun getAttendanceForSubject(subjectTitle: String): List<Attendance> {
        return try {
            val attendanceRef = db.collection("attendance").whereEqualTo("subjectTitle", subjectTitle)
            val querySnapshot = attendanceRef.get().await()
            querySnapshot.toObjects<Attendance>()
        } catch (e: Exception) {
            Log.e("AttendanceViewModel", "Error fetching attendance records", e)
            emptyList()
        }
    }

    suspend fun getTodayAttendanceForSubject(subjectTitle: String, date: String): List<Attendance> {
        return try {
            val attendanceRef = db.collection("attendance")
                .whereEqualTo("subjectTitle", subjectTitle)
                .whereEqualTo("date", date)
            val querySnapshot = attendanceRef.get().await()
            querySnapshot.toObjects<Attendance>()
        } catch (e: Exception) {
            Log.e("AttendanceViewModel", "Error fetching attendance records", e)
            emptyList()
        }
    }

    // Check if attendance is already marked
    suspend fun isAttendanceMarked(studentId: String, subjectTitle: String, date: String): Boolean {
        return try {
            val attendanceRef = db.collection("attendance")
                .whereEqualTo("studentId", studentId)
                .whereEqualTo("subjectTitle", subjectTitle)
                .whereEqualTo("date", date)
            val querySnapshot = attendanceRef.get().await()
            !querySnapshot.isEmpty
        } catch (e: Exception) {
            Log.e("AttendanceViewModel", "Error checking attendance status", e)
            false
        }
    }

    // Fetch all attendance for a specific student in a specific subject
    suspend fun getAttendanceForStudentInSubject(
        studentId: String,
        subjectTitle: String,
        fromDate: Timestamp,
        toDate: Timestamp
    ): List<Attendance> {
        return try {
            Log.v("AttendanceViewModel",fromDate.toString())
            Log.v("AttendanceViewModel",toDate.toString())

            val attendanceRef = db.collection("attendance")
                .whereEqualTo("studentId", studentId)
                .whereEqualTo("subjectTitle", subjectTitle)
                .whereGreaterThanOrEqualTo("date",fromDate)
                .whereLessThanOrEqualTo("date",toDate)
            val querySnapshot = attendanceRef.get().await().toObjects<Attendance>()
            Log.v("AttendanceViewModel",querySnapshot[0].studentId)
            return querySnapshot
        } catch (e: Exception) {
            Log.e("AttendanceViewModel", "Error fetching attendance records", e)
            emptyList()
        }
    }
}