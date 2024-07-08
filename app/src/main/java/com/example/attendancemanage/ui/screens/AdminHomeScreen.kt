package com.example.attendancemanage.ui.screens

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Report
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.attendancemanage.ui.navigation.BottomNavigationBar
import com.example.attendancemanage.ui.navigation.NavigationItem
import com.example.attendancemanage.ui.navigation.Screens
import com.example.attendancemanage.viewmodel.AttendanceViewModel
import com.example.attendancemanage.viewmodel.AuthViewModel
import com.example.attendancemanage.viewmodel.LeaveApprovalViewModel
import com.example.attendancemanage.viewmodel.StudentViewModel
import com.example.attendancemanage.viewmodel.SubjectViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminHomeScreen(
    navController: NavHostController,
) {

    val bottomNavigationItemsList = listOf(
        NavigationItem(
            route = "leave_Approval",
            title = "Leave Approval",
            selectedIcon = Icons.Filled.Home,
            unSelectedIcon = Icons.Filled.Home
        ),
        NavigationItem(
            route = "search",
            title = "Search",
            selectedIcon = Icons.Filled.Search,
            unSelectedIcon = Icons.Filled.Search
        ),
        NavigationItem(
            route = "report",
            title = "Report",
            selectedIcon = Icons.Filled.Report,
            unSelectedIcon = Icons.Filled.Report
        )
    )

    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute by remember(navBackStackEntry) {
        derivedStateOf { navBackStackEntry?.destination?.route }
    }
    val routesWithoutBottomBar = listOf(
        "confirm_screen_route",
        "diseased_result_route",
        "blog_result_route/{blogId}",
        "appInfo",
        "help_center",
        "suggestion",
        "privacy_policy",
        "account_info",
        "edit_account_info",
        "recent_disease_result/{id}"
    )

    Scaffold(
        bottomBar = {
            if (!routesWithoutBottomBar.contains(currentRoute)) {
                BottomNavigationBar(
                    items = bottomNavigationItemsList,
                    currentRoute = currentRoute
                ) { currentNavigationItem ->
                    navController.navigate(currentNavigationItem.route) {
                        navController.graph.startDestinationRoute?.let { startDestinationRoute ->
                            popUpTo(startDestinationRoute) {
                                saveState = true
                            }
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            }
        }
    ) { innerPadding ->
        SetUpNavGraph(
            navController = navController,
            innerPadding = innerPadding,
            onSignOut = { }
        )
    }
}

@Composable
fun SetUpNavGraph(
    navController: NavHostController,
    innerPadding: PaddingValues,
    onSignOut: () -> Unit
) {
    val subjectViewModel: SubjectViewModel = viewModel()
    val attendanceViewModel: AttendanceViewModel = viewModel()
    val authViewModel: AuthViewModel = viewModel()
    val studentViewModel: StudentViewModel = viewModel()
    val leaveApprovalViewModel: LeaveApprovalViewModel = viewModel()

    var currentScreen by remember { mutableStateOf("leave_approval") }

    NavHost(
        navController = navController,
        startDestination = Screens.Leave_Approval.route,
        modifier = Modifier.padding(innerPadding)
    ) {
        composable(Screens.Leave_Approval.route) {
            currentScreen = "leave_approval"
        }
        composable(Screens.Search.route) {
            currentScreen = "search"
        }
        composable(Screens.Report.route) {
            currentScreen = "report"
        }
    }

    Crossfade(targetState = currentScreen) { screen ->
        when (screen) {
            "leave_approval" -> LeaveApprovalScreen(navController,leaveApprovalViewModel,studentViewModel)
            "search" -> SearchScreen(navController,studentViewModel)
            "report" -> ReportScreen(navController,studentViewModel,attendanceViewModel)
        }
    }
}

//
//@Composable
//fun AdminRo(student: Student, onClick: () -> Unit) {
//    Row(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(8.dp)
//            .clickable { onClick() }
//            .background(MaterialTheme.colorScheme.surface)
//            .padding(16.dp),
//        verticalAlignment = Alignment.CenterVertically,
//        horizontalArrangement = Arrangement.SpaceBetween
//    ) {
//        Text(text = student.rollNo, style = MaterialTheme.typography.bodyMedium)
//    }
//}
