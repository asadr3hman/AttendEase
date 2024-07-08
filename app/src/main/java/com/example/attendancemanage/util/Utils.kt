package com.example.attendancemanage.util


import java.util.Calendar
import com.ozcanalasalvar.datepicker.model.Date as DatePickerDate
import com.google.firebase.Timestamp

fun java.util.Date.toDatePickerModel(): DatePickerDate {
    val calendar = Calendar.getInstance()
    calendar.time = this
    return DatePickerDate(
        year = calendar.get(Calendar.YEAR),
        month = calendar.get(Calendar.MONTH),
        day = calendar.get(Calendar.DAY_OF_MONTH)
    )
}


fun java.util.Date.toFirebaseTimestamp(): Timestamp {
    val calendar = Calendar.getInstance()
    calendar.set(Calendar.YEAR, year)
    calendar.set(Calendar.MONTH, month)
    calendar.set(Calendar.DAY_OF_MONTH, day)
    // Clear the time part (hour, minute, second, millisecond)
    calendar.set(Calendar.HOUR_OF_DAY, 0)
    calendar.set(Calendar.MINUTE, 0)
    calendar.set(Calendar.SECOND, 0)
    calendar.set(Calendar.MILLISECOND, 0)
    return Timestamp(calendar.time)
}