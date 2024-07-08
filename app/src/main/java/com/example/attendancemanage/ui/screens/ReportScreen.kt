package com.example.attendancemanage.ui.screens

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavHostController
import com.example.attendancemanage.viewmodel.AttendanceViewModel
import com.example.attendancemanage.viewmodel.StudentViewModel
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.ozcanalasalvar.datepicker.compose.datepicker.WheelDatePicker
import com.ozcanalasalvar.datepicker.model.Date
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Calendar


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportScreen(
    navController: NavHostController,
    studentViewModel: StudentViewModel,
    attendanceViewModel: AttendanceViewModel
) {
    var rollNo by remember { mutableStateOf("") }
    val subjects = listOf("Math", "Science", "History", "English")
    var selectedText by remember { mutableStateOf(subjects[0]) }
    var expanded by remember { mutableStateOf(false) }
    var fromDate by remember { mutableStateOf(Date(2024, 7, 6)) }
    var toDate by remember { mutableStateOf(Date(2024, 7, 6)) }

    val context = LocalContext.current

    // State for dialogs
    var showFromDatePicker by remember { mutableStateOf(false) }
    var showToDatePicker by remember { mutableStateOf(false) }

    // State for report data
    var attendanceReport by remember { mutableStateOf<Pair<Int, Int>?>(null) }

    // Firestore instance
    val db = FirebaseFirestore.getInstance()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        TextField(
            value = rollNo,
            onValueChange = { rollNo = it },
            label = { Text("Roll No") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = {
                expanded = !expanded
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            TextField(
                value = selectedText,
                onValueChange = {},
                readOnly = true,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor()
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.fillMaxWidth()
            ) {
                subjects.forEach { item ->
                    DropdownMenuItem(
                        text = { Text(text = item) },
                        onClick = {
                            selectedText = item
                            expanded = false
                            Toast.makeText(context, item, Toast.LENGTH_SHORT).show()
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "From date:")
        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                showFromDatePicker = true
            }) {
            Text("Select From Date")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "To date:")
        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                showToDatePicker = true
            }) {
            Text("Select To Date")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                // Generate attendance report
                CoroutineScope(Dispatchers.IO).launch {
//                    val attendanceRecords = attendanceViewModel.getAttendanceForStudentInSubject(
//                        rollNo,
//                        selectedText,
//                        fromDate,
//                        toDate
//                    )
//
//                    // Calculate total and attended classes
//                    val totalClasses = attendanceRecords.size
//                    val attendedClasses = attendanceRecords.count { it.status == "Present" }
//
//                    withContext(Dispatchers.Main) {
//                        attendanceReport = Pair(attendedClasses, totalClasses)
//                    }
                }
            },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text(text = "Generate")
        }

        Spacer(modifier = Modifier.height(16.dp))

        attendanceReport?.let { (attendedClasses, totalClasses) ->
            Text(text = "Total Classes: $totalClasses")
            Text(text = "Attended Classes: $attendedClasses")
        }

        // Show DatePickerDialogs
        if (showFromDatePicker) {
            DatePickerDialog(
                showDialog = showFromDatePicker,
                onDismissRequest = { showFromDatePicker = false },
                initialDate = fromDate,
                onDateSelected = { date ->
                    fromDate = date
                    showFromDatePicker = false
                }
            )
        }

        if (showToDatePicker) {
            DatePickerDialog(
                showDialog = showToDatePicker,
                onDismissRequest = { showToDatePicker = false },
                initialDate = toDate,
                onDateSelected = { date ->
                    toDate = date
                    showToDatePicker = false
                }
            )
        }
    }
}

//private fun generateReport(
//    rollNo: String,
//    subjectTitle: String,
//    fromDate: Date,
//    toDate: Date,
//    db: FirebaseFirestore,
//    onReportGenerated: (Pair<Int, Int>) -> Unit
//) {
//    db.collection("students")
//        .whereEqualTo("rollNo", rollNo)
//        .get()
//        .addOnSuccessListener { studentDocuments ->
//            if (studentDocuments.isEmpty) {
//                onReportGenerated(Pair(0, 0))
//                return@addOnSuccessListener
//            }
//
//            val studentId = studentDocuments.documents[0].id
//
//            db.collection("attendance")
//                .whereEqualTo("studentId", studentId)
//                .whereEqualTo("subjectTitle", subjectTitle)
//                .whereGreaterThanOrEqualTo("date", fromDate)
//                .whereLessThanOrEqualTo("date", toDate)
//                .get()
//                .addOnSuccessListener { attendanceDocuments ->
//                    val totalClasses = attendanceDocuments.size()
//                    val attendedClasses = attendanceDocuments.count { doc ->
//                        doc.getString("status") == "Present"
//                    }
//
//                    onReportGenerated(Pair(attendedClasses, totalClasses))
//                }
//        }
//}
@Composable
fun DatePickerDialog(
    showDialog: Boolean,
    onDismissRequest: () -> Unit,
    initialDate: Date,
    onDateSelected: (Date) -> Unit
) {
    if (showDialog) {
        Dialog(
            onDismissRequest = onDismissRequest,
            properties = DialogProperties(dismissOnBackPress = true, dismissOnClickOutside = true)
        ) {
            Surface(
                shape = MaterialTheme.shapes.medium,
                color = MaterialTheme.colorScheme.surface,
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    var selectedDate by remember { mutableStateOf(initialDate) }

                    WheelDatePicker(
                        offset = 3,
                        yearsRange = IntRange(1900, 2100),
                        startDate = selectedDate,
                        textSize = 18,
                        selectorEffectEnabled = true,
                        darkModeEnabled = false,
                        onDateChanged = { day, month, year, date ->
                            selectedDate = Date(date)
                        }
                    )

                    Spacer(modifier = Modifier.padding(8.dp))

                    Button(
                        onClick = {
                            onDateSelected(selectedDate)
                            onDismissRequest()
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Select Date")
                    }
                }
            }
        }
    }
}