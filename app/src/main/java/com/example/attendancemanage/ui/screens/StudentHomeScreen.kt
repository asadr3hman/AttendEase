package com.example.attendancemanage.ui.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.attendancemanage.model.Subject
import com.example.attendancemanage.ui.components.showToast
import com.example.attendancemanage.ui.theme.AttendanceManageTheme
import com.example.attendancemanage.viewmodel.StudentViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudentHomeScreen(
    navController: NavHostController,
    studentViewModel: StudentViewModel,
    studentuid: String
) {
    val context = LocalContext.current
    val subjects = remember { mutableStateListOf<Subject>() }
    if (studentuid != null) {
        Log.v("StudentHomeSCREEN", studentuid)
    }

    // Fetch the student data using LaunchedEffect
    LaunchedEffect(studentuid) {
        Log.v("StudentHomeSCREEN", "HEHEHE")
        val student = studentViewModel.getStudentData(studentuid)
        student?.let { Log.v("StudentHomeSCREEN", it.name.toString()) }
        student?.subjects?.let {
            subjects.clear()
            subjects.addAll(it)
        }
    }

    AttendanceManageTheme {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Student Dashboard") },
                    actions = {
                        IconButton(onClick = { showToast(context,"Edit Profile") }) {
                            Icon(
                                imageVector = Icons.Default.AccountCircle,
                                contentDescription = "Edit Profile"
                            )
                        }
                    }
                )
            }
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text("Your Subjects", style = MaterialTheme.typography.headlineSmall)
                LazyColumn {
                    items(subjects) { subject ->
                        SubjectRo(subject, onClick = {
                            navController.navigate("subject/${subject.subjectTitle}/${studentuid}")
                        })
                    }
                }
            }
        }
    }
}
@Composable
fun SubjectRo(subject: Subject, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onClick() }
            .background(MaterialTheme.colorScheme.surface)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = subject.subjectTitle, style = MaterialTheme.typography.bodyMedium)
    }
}