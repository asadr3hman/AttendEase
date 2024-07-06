package com.example.attendancemanage.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.ozcanalasalvar.datepicker.compose.datepicker.WheelDatePicker
import com.ozcanalasalvar.datepicker.model.Date
import java.text.SimpleDateFormat
import java.util.Locale


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportScreen() {
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

    // Date format
    val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    var userFormdata by remember { mutableStateOf("") }
    var userTodata by remember { mutableStateOf("") }



    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        TextField(
            value = rollNo,
            onValueChange = { rollNo = it },
            label = { Text("Roll No") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
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
        Text(text = userFormdata)

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "To date:")
        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = {
            showToDatePicker = true
        }) {
            Text("Select To Date")
        }
        Text(text = userTodata)

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                // Handle generate report action
                Toast.makeText(context, "Generate Report", Toast.LENGTH_SHORT).show()
            },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text(text = "Generate")
        }

        // Show DatePickerDialogs
        DatePickerDialog(
            showDialog = showFromDatePicker,
            onDismissRequest = { showFromDatePicker = false },
            initialDate = fromDate,
            onDateSelected = {
                date -> fromDate = date
                Toast.makeText(context,toDate.year.toString(),Toast.LENGTH_SHORT).show()
            }
        )

        DatePickerDialog(
            showDialog = showToDatePicker,
            onDismissRequest = { showToDatePicker = false },
            initialDate = toDate,
            onDateSelected = {
                date -> toDate = date
                Toast.makeText(context,toDate.year.toString(),Toast.LENGTH_SHORT).show()
            }
        )
    }
}

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