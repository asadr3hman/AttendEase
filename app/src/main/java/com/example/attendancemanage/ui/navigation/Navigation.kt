package com.example.attendancemanage.ui.navigation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.attendancemanage.model.Student
import com.example.attendancemanage.ui.auth.validateSignUp
import com.example.attendancemanage.ui.components.showToast
import com.example.attendancemanage.ui.screens.AdminHomeScreen
import com.example.attendancemanage.ui.screens.LoginScreen
import com.example.attendancemanage.ui.screens.SignUpScreen
import com.example.attendancemanage.ui.screens.StudentHomeScreen
import com.example.attendancemanage.ui.screens.SubjectChooseScreen
import com.example.attendancemanage.ui.screens.SubjectManageScreen
import com.example.attendancemanage.viewmodel.AttendanceViewModel
import com.example.attendancemanage.viewmodel.AuthViewModel
import com.example.attendancemanage.viewmodel.StudentViewModel
import com.example.attendancemanage.viewmodel.SubjectViewModel
import kotlinx.coroutines.launch

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val subjectViewModel: SubjectViewModel = viewModel()
    val attendanceViewModel: AttendanceViewModel = viewModel()
    val authViewModel: AuthViewModel = viewModel()
    val studentViewModel: StudentViewModel = viewModel()
    LaunchedEffect(key1 = Unit) {
        val user = authViewModel.getSignedInUser()
        if (user != null) {
            // Fetch user role
            Log.v("NAV","")
            val role = authViewModel.getUserRole(user.userId)
            Log.v("Signup", role!!)
            when (role) {
                "Student" -> {
                    navController.navigate("student_home/${user.userId}") {
                        popUpTo("login") { inclusive = true }
                    }
                }

                "Admin" -> {
                    navController.navigate("admin_home") {
                        popUpTo("login") { inclusive = true }
                    }
                }

                else -> {
                    navController.navigate("login") {
                        popUpTo("login") { inclusive = true }
                    }
                }
            }
        } else {
            navController.navigate("login") { popUpTo("login") { inclusive = true } }
        }
    }

    NavHost(navController, startDestination = "login") {
        composable("login") { Login(navController, authViewModel) }
        composable("register") { Register(navController, authViewModel) }
        composable("subject_choose/{username}/{email}/{rollNo}/{uid}") {
            val name = it.arguments?.getString("username")
            val rollNo = it.arguments?.getString("rollNo")
            val email = it.arguments?.getString("email")
            val uid = it.arguments?.getString("uid")
            val student = Student(email!!, rollNo!!, name!!, uid = uid)
            Log.v("NavHost", student.toString())
            Subject_choose(navController, subjectViewModel, studentViewModel, student)
        }
        composable("student_home/{uid}") {
            val studentuid = it.arguments?.getString("uid")
            StudentHome(navController, subjectViewModel, studentViewModel, studentuid!!)
        }
        composable("subject/{name}/{studentId}") {
            val name = it.arguments?.getString("name")
            val rollNo = it.arguments?.getString("studentId")
            SubjectManage(navController, attendanceViewModel, name!!, rollNo!!)
        }
//        composable("mark_attendance") { MarkAttendanceScreen(navController) }
//        composable("view_attendance") { ViewAttendanceScreen(navController) }
//        composable("leave_request") { LeaveRequestScreen(navController) }
//        composable("edit_profile") { EditProfileScreen(navController) }

        composable("admin_home") { AdminHome(navController) }
//        composable("manage_attendance") { ManageAttendanceScreen(navController) }
//        composable("manage_leave_requests") { ManageLeaveRequestsScreen(navController) }
//        composable("generate_reports") { GenerateReportsScreen(navController) }
//        composable("grading_module") { GradingModuleScreen(navController) }
    }
}

@Composable
fun AdminHome(navController: NavHostController) {
    AdminHomeScreen(navController)
    val context = LocalContext.current
    showToast(context, "AdminHome")
}

@Composable
fun SubjectManage(
    navController: NavHostController,
    attendanceViewModel: AttendanceViewModel,
    name: String,
    rollNo: String
) {
    SubjectManageScreen(
        navController,
        attendanceViewModel,
        name,
        rollNo
    )
}

@Composable
fun StudentHome(
    navController: NavHostController,
    subjectViewModel: SubjectViewModel,
    studentViewModel: StudentViewModel,
    studentUID: String,
) {
    StudentHomeScreen(navController, studentViewModel = studentViewModel, studentUID)
}

@Composable
fun Subject_choose(
    navController: NavHostController,
    subjectViewModel: SubjectViewModel,
    studentViewModel: StudentViewModel,
    student: Student
) {
    SubjectChooseScreen(navController, subjectViewModel, onNextClick = {
        val subjects = subjectViewModel.selectedSubjects
        student.subjects = subjects
        Log.v("SubjectChoose", student.toString())
        studentViewModel.addStudentWithSubjects(student)
        navController.navigate("student_home/{${student.uid}}") {
            popUpTo("login") { inclusive = true }
        }
    })
}

@Composable
fun Register(navController: NavController, authViewModel: AuthViewModel) {
    val context = LocalContext.current
    var loading by rememberSaveable { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    SignUpScreen(onSignUpClicked = { username, email, password, confirmPassword, rollNo, setLoading ->
        val validationError = validateSignUp(username, email, password, confirmPassword)
        if (validationError != null) {
            showToast(context, validationError)
        } else {
            coroutineScope.launch {
                authViewModel.signUpUser(username, email, password) { success, uid, exception ->
                    if (success) {
                        showToast(context, "Successful")
                        navController.navigate("subject_choose/$username/$email/$rollNo/$uid") {
                            popUpTo("register") {
                                inclusive = true
                            }
                        }
                    } else {
                        exception?.let { showToast(context, it.toString()) }
                        setLoading(false) // Hide loading animation when sign-up fails
                    }
                }
            }


        }
    }, signInNavigation = {
        navController.navigate("login") { popUpTo("register") { inclusive = true } }
    }, loading = loading, setLoading = { loading = it })
}


@Composable
fun Login(navController: NavController, authViewModel: AuthViewModel) {
    var loading by rememberSaveable { mutableStateOf(false) }
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    LoginScreen(onForgetPassClicked = {
        showToast(context, "Forget Password")
    }, onLoginClicked = { username, password, setLoading ->
        coroutineScope.launch {
            setLoading(true)
            Log.v("Auth", "Starting Auth")
            authViewModel.signInWithEmailPassword(username, password).let { signInResult ->
                if (signInResult.data != null) {
                    Log.d("SignIn", "User signed in successfully: ${signInResult.data}")
                    setLoading(false)
                    val user = signInResult
                    if (user != null) {
                        // Fetch user role
                        val role = user.data?.let { authViewModel.getUserRole(it.userId) }
                        Log.v("Signup", role!!)
                        when (role) {
                            "Student" -> {
                                navController.navigate("student_home/${user.data.userId}") {
                                    popUpTo("login") { inclusive = true }
                                }
                            }

                            "Admin" -> {
                                navController.navigate("admin_home") {
                                    popUpTo("login") { inclusive = true }
                                }
                            }

                            else -> {
                                navController.navigate("login") {
                                    popUpTo("login") { inclusive = true }
                                }
                            }
                        }
                    } else {
                        navController.navigate("login") { popUpTo("login") { inclusive = true } }
                    }
                } else {
                    Log.e("SignIn", "Failed to sign in user: ${signInResult.errorMessage}")
                    signInResult.errorMessage?.let { showToast(context, it) }
                    setLoading(false) // Hide loading animation when sign-in fails
                }
            }
        }
        Log.v("Navigation", username + password)
    }, signUpNavigation = {
        navController.navigate("register") { popUpTo("login") { inclusive = true } }
    }, loading = loading, setLoading = { loading = it })
}
