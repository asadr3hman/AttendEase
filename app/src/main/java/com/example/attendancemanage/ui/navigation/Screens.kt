package com.example.attendancemanage.ui.navigation

sealed class Screens(var route: String) {

    object  Leave_Approval : Screens("leave_Approval")
    object  Report : Screens("report")
    object  Search : Screens("search")
}