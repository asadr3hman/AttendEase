package com.example.attendancemanage.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.attendancemanage.model.Attendance
import com.example.attendancemanage.viewmodel.AttendanceViewModel
import com.example.attendancemanage.viewmodel.SubjectViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubjectManageScreen(
    navController: NavHostController, attendanceViewModel: AttendanceViewModel, name: String,rollNo: String
) {
    val context = LocalContext.current
    val currentDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
    Scaffold(topBar = {
        TopAppBar(
            title = { Text(text = name, color = MaterialTheme.colorScheme.onBackground) },
            navigationIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background)
        )
    }) { innerPadding ->
        val modifier = Modifier.padding(innerPadding)
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
                    val attendance = Attendance(rollNo, name, currentDate, "Present")
                    attendanceViewModel.markAttendance(attendance)
                    Toast.makeText(context, "Attendance marked as Present", Toast.LENGTH_SHORT).show()
                }, modifier = Modifier.fillMaxWidth()
            ) {
                Text("Mark Attendance")
            }
            Button(
                onClick = { /* Handle Mark Leave */ }, modifier = Modifier.fillMaxWidth()
            ) {
                Text("Mark Leave")
            }
            Button(
                onClick = { /* Handle View Attendance */ }, modifier = Modifier.fillMaxWidth()
            ) {
                Text("View Attendance")
            }
        }
    }
}
