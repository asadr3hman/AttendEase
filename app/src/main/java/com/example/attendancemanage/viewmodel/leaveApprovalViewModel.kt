package com.example.attendancemanage.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.attendancemanage.model.LeaveRequest
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class LeaveApprovalViewModel : ViewModel() {
    private val _leaveRequests = MutableStateFlow<List<LeaveRequest>>(emptyList())
    val leaveRequests: StateFlow<List<LeaveRequest>> = _leaveRequests.asStateFlow()

    private val db = FirebaseFirestore.getInstance()

    init {
        fetchLeaveRequests()
    }

    private fun fetchLeaveRequests() {
        db.collection("leaveRequests").get().addOnSuccessListener { documents ->
            val leaveRequestList = documents.map { document ->
                document.toObject(LeaveRequest::class.java)
            }
            _leaveRequests.value = leaveRequestList
        }.addOnFailureListener { exception ->
            Log.e("LeaveApprovalViewModel", "Error fetching leave requests", exception)
        }
    }

    fun approveLeaveRequest(leaveRequest: LeaveRequest) {
        viewModelScope.launch {
            val updatedLeaveRequest = leaveRequest.copy(status = "Approved")
            updateLeaveRequestInFirestore(updatedLeaveRequest)
            updateAttendanceStatus(updatedLeaveRequest)
            // Remove the request from the list
            _leaveRequests.value = _leaveRequests.value.filter { it != leaveRequest }
        }
    }

    fun rejectLeaveRequest(leaveRequest: LeaveRequest) {
        viewModelScope.launch {
            val updatedLeaveRequest = leaveRequest.copy(status = "Rejected")
            updateLeaveRequestInFirestore(updatedLeaveRequest)
            // Remove the request from the list
            _leaveRequests.value = _leaveRequests.value.filter { it != leaveRequest }
        }
    }

    private suspend fun updateLeaveRequestInFirestore(leaveRequest: LeaveRequest) {
        try {
            val leaveRequestRef = db.collection("leaveRequests")
                .whereEqualTo("studentId", leaveRequest.studentId)
                .whereEqualTo("date", leaveRequest.date)
                .get()
                .await()

            // There should ideally be only one document matching studentId and date
            for (document in leaveRequestRef.documents) {
                val leaveRequestDocRef = db.collection("leaveRequests").document(document.id)
                leaveRequestDocRef.update("status", leaveRequest.status).await()
            }
        } catch (e: Exception) {
            Log.e("ViewModel", "Error updating leave request status", e)
        }
    }

    private suspend fun updateAttendanceStatus(leaveRequest: LeaveRequest) {
        try {
            val attendanceRef = db.collection("attendance")
                .whereEqualTo("studentId", leaveRequest.studentId)
                .whereEqualTo("date", leaveRequest.date)
                .whereEqualTo("subjectTitle", leaveRequest.subjectTitle)
                .get()
                .await()

            // Update each document found
            for (document in attendanceRef.documents) {
                val attendanceDocRef = db.collection("attendance").document(document.id)
                attendanceDocRef.update("status", leaveRequest.status).await()
            }
        } catch (e: Exception) {
            Log.e("ViewModel", "Error updating attendance status", e)
        }
    }
}
