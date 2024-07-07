package com.example.attendancemanage.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.attendancemanage.model.Attendance
import com.example.attendancemanage.model.LeaveRequest
import com.example.attendancemanage.viewmodel.AttendanceViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubjectManageScreen(
    navController: NavHostController,
    attendanceViewModel: AttendanceViewModel,
    subjectName: String,
    rollNo: String
) {
    val context = LocalContext.current
    val currentDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
    val viewModelScope = rememberCoroutineScope()
    var isAttendanceMarked by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        isAttendanceMarked = attendanceViewModel.isAttendanceMarked(rollNo, subjectName, currentDate)
        isLoading = false
    }

    Scaffold(topBar = {
        TopAppBar(
            title = { Text(text = subjectName, color = MaterialTheme.colorScheme.onBackground) },
            navigationIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background)
        )
    }) { innerPadding ->
        val modifier = Modifier.padding(innerPadding)

        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = MaterialTheme.colorScheme.background)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Button(
                    onClick = {
                        val attendance = Attendance(
                            studentId = rollNo,
                            subjectTitle = subjectName,
                            date = currentDate,
                            status = "Present"
                        )
                        viewModelScope.launch {
                            attendanceViewModel.markAttendance(attendance)
                        }
                        Toast.makeText(context, "Attendance marked as Present", Toast.LENGTH_SHORT)
                            .show()
                        isAttendanceMarked = true
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !isAttendanceMarked
                ) {
                    Text("Mark Attendance")
                }
                Button(
                    onClick = {
                        val attendance = Attendance(
                            studentId = rollNo,
                            subjectTitle = subjectName,
                            date = currentDate,
                            status = "Absent"
                        )
                        viewModelScope.launch {
                            attendanceViewModel.markAttendance(attendance)
                        }
                        Toast.makeText(context, "Attendance marked as Absent", Toast.LENGTH_SHORT)
                            .show()
                        isAttendanceMarked = true
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !isAttendanceMarked
                ) {
                    Text("Mark Absent")
                }
                Button(
                    onClick = {
                        val attendance = LeaveRequest(
                            studentId = rollNo,
                            subjectTitle = subjectName,
                            date = currentDate
                        )
                        viewModelScope.launch {
                            attendanceViewModel.requestLeave(attendance)
                        }
                        Toast.makeText(context, "Leave Request sent", Toast.LENGTH_SHORT)
                            .show()
                        isAttendanceMarked = true
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !isAttendanceMarked
                ) {
                    Text("Request Leave")
                }
                Button(
                    onClick = {
                        navController.navigate("view_attendance/${subjectName}")
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("View Attendance")
                }
            }
        }
    }
}
