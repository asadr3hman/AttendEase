package com.example.attendancemanage.ui.screens

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Clear
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.attendancemanage.R
import com.example.attendancemanage.model.Subject
import com.example.attendancemanage.ui.theme.AttendanceManageTheme
import com.example.attendancemanage.viewmodel.SubjectViewModel


@Composable
fun SubjectChooseScreen(
    navController: NavHostController,
    subjectViewModel: SubjectViewModel = viewModel()
) {
    var showAddSubjectDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current

    AttendanceManageTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(horizontal = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                modifier = Modifier.padding(10.dp),
                text = "Choose Subjects",
                style = androidx.compose.ui.text.TextStyle(
                    fontFamily = FontFamily(Font(R.font.roboto_medium, FontWeight.Medium)),
                    fontSize = 24.sp,
                    color = MaterialTheme.colorScheme.onBackground
                )
            )
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                items(
                    subjectViewModel.selectedSubjects.size,
                    key = { index -> subjectViewModel.selectedSubjects[index].subjectTitle + index }) { index ->
                    AnimatedVisibility(
                        visible = true,
                        enter = expandVertically(animationSpec = tween(300)) + fadeIn(
                            animationSpec = tween(
                                300
                            )
                        ),
                        exit = shrinkVertically(animationSpec = tween(300)) + fadeOut(
                            animationSpec = tween(
                                300
                            )
                        )
                    ) {
                        SubjectRow(subjectViewModel.selectedSubjects[index], onRemove = {
                            subjectViewModel.removeSubject(subjectViewModel.selectedSubjects[index])
                        })
                    }
                }
                item {
                    AddSubjectRow(onAdd = { showAddSubjectDialog = true })
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                onClick = {
                    navController.navigate("student_home") {
                        popUpTo("subject_choose") {
                            inclusive = true
                        }
                    }
                }
            ) {
                Text(
                    text = "Next",
                    style = androidx.compose.ui.text.TextStyle(
                        fontFamily = FontFamily(Font(R.font.roboto_medium, FontWeight.Medium)),
                        fontSize = 18.sp,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                )
            }
        }
    }

    if (showAddSubjectDialog) {
        AvailableSubjectsScreen(onSubjectSelected = { selectedSubject ->
            if (subjectViewModel.selectedSubjects.any { it.subjectTitle == selectedSubject.subjectTitle }) {
                Toast.makeText(context, "Subject already selected", Toast.LENGTH_SHORT).show()
            } else {
                subjectViewModel.addSubject(selectedSubject)
            }
            showAddSubjectDialog = false
        }, onDismiss = {
            showAddSubjectDialog = false
        })
    }
}

@Composable
fun AvailableSubjectsScreen(onSubjectSelected: (Subject) -> Unit, onDismiss: () -> Unit) {
    val availableSubjects = listOf(
        Subject("Math", "Dr. John", 4),
        Subject("Physics", "Dr. Smith", 3),
        Subject("Chemistry", "Dr. Doe", 4)
    )

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(8.dp),
            color = MaterialTheme.colorScheme.background,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Select a Subject",
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                LazyColumn {
                    items(availableSubjects) { subject ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp)
                                .clickable {
                                    onSubjectSelected(subject)
                                },
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = subject.subjectTitle,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = onDismiss) {
                    Text("Cancel")
                }
            }
        }
    }
}

@Composable
fun AddSubjectRow(onAdd: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onAdd()
            }
            .background(MaterialTheme.colorScheme.surface)
            .padding(10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Icon(
            modifier = Modifier
                .size(35.dp),
            imageVector = Icons.Outlined.Add,
            contentDescription = "Add",
            tint = MaterialTheme.colorScheme.primary
        )
    }
    Spacer(modifier = Modifier.height(8.dp))
}

@Composable
fun SubjectRow(subject: Subject, onRemove: () -> Unit) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface)
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = subject.subjectTitle,
                modifier = Modifier.weight(1f),
                style = androidx.compose.ui.text.TextStyle(
                    fontFamily = FontFamily(Font(R.font.roboto_medium, FontWeight.Medium)),
                    fontSize = 18.sp,
                    color = MaterialTheme.colorScheme.onBackground
                )
            )
            IconButton(onClick = onRemove) {
                Icon(
                    imageVector = Icons.Outlined.Clear,
                    contentDescription = "Remove",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
    }
}