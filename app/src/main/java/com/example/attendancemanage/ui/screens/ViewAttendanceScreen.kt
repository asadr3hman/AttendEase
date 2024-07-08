package com.example.attendancemanage.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.attendancemanage.R
import com.example.attendancemanage.model.Attendance
import com.example.attendancemanage.model.Student
import com.example.attendancemanage.viewmodel.AttendanceViewModel
import com.example.attendancemanage.viewmodel.StudentViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ViewAttendanceScreen(
    navHostController: NavHostController,
    attendanceViewModel: AttendanceViewModel,
    studentViewModel: StudentViewModel,
    subjectName: String
) {
    val listOfAttendance = remember { mutableStateListOf<Pair<Attendance, Student?>>() }
    val isLoading = remember { mutableStateOf(true) }
    val currentDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        scope.launch {
            val attendanceList = attendanceViewModel.getTodayAttendanceForSubject(subjectName, currentDate)
            listOfAttendance.clear()
            attendanceList.forEach { attendance ->
                val student = studentViewModel.getStudentData(attendance.studentId)
                listOfAttendance.add(Pair(attendance, student))
            }
            isLoading.value = false
        }
    }

    Scaffold(topBar = {
        TopAppBar(
            title = { Text(text = "View Attendance") },
            navigationIcon = {
                IconButton(onClick = { navHostController.popBackStack() }) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                }
            }
        )
    }) { innerPadding ->
        if (isLoading.value) {
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
                    "Attendance List",
                    style = TextStyle(
                        fontFamily = FontFamily(Font(R.font.roboto_medium, FontWeight.Medium)),
                        fontSize = 18.sp,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                )
                LazyColumn {
                    items(listOfAttendance) { (attendance, student) ->
                        ListItem(
                            headlineContent = { Text(text = student?.name ?: "Unknown Student") },
                            supportingContent = { Text(text = student?.rollNo ?: attendance.studentId) },
                            trailingContent = { Text(text = attendance.status) }
                        )
                    }
                }
            }
        }
    }
}