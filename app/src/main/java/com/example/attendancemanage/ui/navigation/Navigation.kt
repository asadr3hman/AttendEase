package com.example.attendancemanage.ui.navigation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.attendancemanage.ui.auth.validateSignUp
import com.example.attendancemanage.ui.components.showToast
import com.example.attendancemanage.ui.screens.LoginScreen
import com.example.attendancemanage.ui.screens.SignUpScreen
import com.example.attendancemanage.ui.screens.SubjectChooseScreen
import com.example.attendancemanage.viewmodel.AuthViewModel

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController, startDestination = "subject_choose") {
        composable("login") { Login(navController) }
        composable("register") { Register(navController) }
        composable("subject_choose") { Subject_choose(navController) }
//        composable("student_home") { StudentHomeScreen(navController) }
//        composable("admin_home") { AdminHomeScreen(navController) }
//        composable("mark_attendance") { MarkAttendanceScreen(navController) }
//        composable("view_attendance") { ViewAttendanceScreen(navController) }
//        composable("leave_request") { LeaveRequestScreen(navController) }
//        composable("edit_profile") { EditProfileScreen(navController) }
//        composable("manage_attendance") { ManageAttendanceScreen(navController) }
//        composable("manage_leave_requests") { ManageLeaveRequestsScreen(navController) }
//        composable("generate_reports") { GenerateReportsScreen(navController) }
//        composable("grading_module") { GradingModuleScreen(navController) }
    }
}

@Composable
fun Subject_choose(navController: NavHostController) {
    SubjectChooseScreen()
}

@Composable
fun Register(navController: NavController) {
    val context = LocalContext.current
    var loading by rememberSaveable { mutableStateOf(false) }

    SignUpScreen(onSignUpClicked = { username, email, password, confirmPassword, setLoading ->
        val validationError = validateSignUp(username, email, password, confirmPassword)
        if (validationError != null) {
            showToast(context, validationError)
        } else {
//                setLoading(true)
            navController.navigate("subject_choose") {
                popUpTo("register") {
                    inclusive = true
                }
            }

        }
    }, signInNavigation = {
        navController.navigate("login") { popUpTo("register") { inclusive = true } }
    }, loading = loading, setLoading = { loading = it })
}


@Composable
fun Login(navController: NavController) {
    val authViewModel = viewModel<AuthViewModel>()
    var loading by rememberSaveable { mutableStateOf(false) }
    val context = LocalContext.current

    LoginScreen(onForgetPassClicked = {
        navController.navigate("forgetPassword") { popUpTo("login") { inclusive = true } }
    }, onLoginClicked = { username, password, setLoading ->
//            setLoading(true)
        Log.v("Navigation", username + password)
    }, signUpNavigation = {
        navController.navigate("register") { popUpTo("login") { inclusive = true } }
    }, loading = loading, setLoading = { loading = it })
}
