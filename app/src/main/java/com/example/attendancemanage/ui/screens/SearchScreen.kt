package com.example.attendancemanage.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import com.example.attendancemanage.ui.components.ItemList
import com.example.attendancemanage.ui.components.SearchBar
import com.example.attendancemanage.ui.components.showToast
import com.example.attendancemanage.viewmodel.StudentViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun SearchScreen(
    navHostController: NavHostController, studentViewModel: StudentViewModel
) {
    val context = LocalContext.current
    var isLoading by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val students by studentViewModel.studentList.observeAsState()
    val getstudents by studentViewModel.getSomeStudentList.observeAsState()
    var displayList = getstudents ?: students
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
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                SearchBar(onSearch = { searchQuery ->
                    isLoading = true
                    scope.launch {
                        studentViewModel.searchStudent(searchQuery)
                        isLoading = false
                        if (students.isNullOrEmpty()) {
                            displayList = getstudents
                            showToast(context, "No results found, showing some students")
                        }
                    }
                })
                displayList?.let {
                    ItemList(it, onItemClick = { student ->
                        showToast(context, "Student Clicked")
                    })
                }
            }
        }
    }
}