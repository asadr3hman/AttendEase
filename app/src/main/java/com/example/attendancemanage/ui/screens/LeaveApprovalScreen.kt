package com.example.attendancemanage.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.attendancemanage.R
import com.example.attendancemanage.model.Student
import com.example.attendancemanage.viewmodel.LeaveApprovalViewModel
import com.example.attendancemanage.viewmodel.StudentViewModel

@Composable
fun LeaveApprovalScreen(
    navHostController: NavHostController,
    leaveApprovalViewModel: LeaveApprovalViewModel,
    studentViewModel: StudentViewModel
) {
    val listOfLeaveRequests by leaveApprovalViewModel.leaveRequests.collectAsState()
    var isLoading by remember { mutableStateOf(true) }
    isLoading = listOfLeaveRequests.isEmpty()
    Scaffold { innerPadding ->
        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    "Leave Requests",
                    style = TextStyle(
                        fontFamily = FontFamily(Font(R.font.roboto_medium, FontWeight.Medium)),
                        fontSize = 18.sp,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                )
                LazyColumn {
                    items(listOfLeaveRequests) { leaveRequest ->
                        if (leaveRequest.status == "Pending") {
                            val studentState = produceState<Student?>(initialValue = null, leaveRequest.studentId) {
                                value = studentViewModel.getStudentData(leaveRequest.studentId)
                            }
                            val student = studentState.value
                            if (student != null) {
                                ListItem(
                                    headlineContent = { Text(text = student.name) },
                                    overlineContent = { Text(text = student.rollNo) },
                                    supportingContent = { Text(text = leaveRequest.subjectTitle + " " + leaveRequest.date) },
                                    trailingContent = {
                                        Row {
                                            IconButton(onClick = {
                                                leaveApprovalViewModel.approveLeaveRequest(leaveRequest)
                                            }) {
                                                Icon(Icons.Default.Check, contentDescription = "Approve")
                                            }
                                            IconButton(onClick = {
                                                leaveApprovalViewModel.rejectLeaveRequest(leaveRequest)
                                            }) {
                                                Icon(Icons.Default.Close, contentDescription = "Reject")
                                            }
                                        }
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
