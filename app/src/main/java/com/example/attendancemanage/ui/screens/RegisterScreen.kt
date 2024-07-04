package com.example.attendancemanage.ui.screens

import android.content.Context
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.attendancemanage.R
import com.example.attendancemanage.ui.auth.validateSignUp
import com.example.attendancemanage.ui.components.ScreenTextFeild
import com.example.attendancemanage.ui.components.cornerRadius
import com.example.attendancemanage.ui.components.showToast
import com.example.attendancemanage.ui.components.textFieldPadding
import com.example.attendancemanage.ui.theme.AttendanceManageTheme

@Composable
fun SignUpScreen(
    onSignUpClicked: (String, String, String, String,String, (Boolean) -> Unit) -> Unit,
    signInNavigation: () -> Unit,
    loading: Boolean,
    setLoading: (Boolean) -> Unit,
    context: Context = LocalContext.current
) {


    AttendanceManageTheme {
        var userName by rememberSaveable {
            mutableStateOf("")
        }
        var userRollno by rememberSaveable {
            mutableStateOf("")
        }
        var userEmail by rememberSaveable {
            mutableStateOf("")
        }
        var userPassword by rememberSaveable {
            mutableStateOf("")
        }
        var confirmUserPassword by rememberSaveable {
            mutableStateOf("")
        }
        Column(
            modifier = Modifier
                .background(color = MaterialTheme.colorScheme.background)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    modifier = Modifier.padding(top = 24.dp),
                    text = "Create Account",
                    style = TextStyle(
                        fontFamily = FontFamily(Font(R.font.roboto_bold, FontWeight.Bold)),
                        fontSize = 30.sp,
                        letterSpacing = 1.sp,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                )
                Text(
                    modifier = Modifier.padding(top = 4.dp),
                    text = "Sign up to get started",
                    style = TextStyle(
                        fontFamily = FontFamily(Font(R.font.roboto, FontWeight.Normal)),
                        fontSize = 18.sp,
                        letterSpacing = 1.sp,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                )

                ScreenTextFeild(
                    text = userName,
                    hint = "Full Name",
                    leadingIcon = Icons.Outlined.AccountCircle,
                    false
                ) {
                    userName = it
                }
                ScreenTextFeild(
                    text = userRollno, hint = "Roll no", leadingIcon = Icons.Outlined.Person, false
                ) {
                    userRollno = it
                }
                ScreenTextFeild(
                    text = userEmail,
                    hint = "Enter Email",
                    leadingIcon = Icons.Outlined.Email,
                    false
                ) {
                    userEmail = it
                }
                ScreenTextFeild(
                    text = userPassword,
                    hint = "Enter Password",
                    leadingIcon = Icons.Outlined.Lock,
                    true
                ) {
                    userPassword = it
                }
                ScreenTextFeild(
                    text = confirmUserPassword,
                    hint = "Re-Enter Password",
                    leadingIcon = Icons.Outlined.Lock,
                    true
                ) {
                    confirmUserPassword = it
                }
                Button(modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = textFieldPadding, end = textFieldPadding, top = textFieldPadding
                    ),
                    shape = RoundedCornerShape(cornerRadius),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                    onClick = {
//                    showToast(context = context, message = "Click: Button")
                        val validateSignUp =
                            validateSignUp(userName, userEmail, userPassword, confirmUserPassword)
                        if (validateSignUp == null) {
                            onSignUpClicked.invoke(
                                userName, userEmail, userPassword, confirmUserPassword,userRollno, setLoading
                            )
                        } else {
                            showToast(
                                context, validateSignUp
                            )
                        }
                    }) {
                    if (loading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            color = MaterialTheme.colorScheme.primary,
                            trackColor = Color.White
                        )
                    } else {
                        Text(
                            text = "Sign Up", style = TextStyle(
                                fontFamily = FontFamily(
                                    Font(
                                        R.font.roboto_bold, FontWeight.Medium
                                    )
                                ), fontSize = 18.sp, color = MaterialTheme.colorScheme.onPrimary
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(48.dp))

                Spacer(modifier = Modifier.height(20.dp))
                val textBottom1 = "Already a member? "
                val textBottom2 = "Sign In"
                Spacer(modifier = Modifier.height(20.dp))

                Row(
                    modifier = Modifier.padding(bottom = 8.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = textBottom1,
                        color = MaterialTheme.colorScheme.onBackground,
                        fontFamily = FontFamily(
                            Font(
                                R.font.roboto_medium, weight = FontWeight.Medium
                            )
                        ),
                        fontSize = 16.sp
                    )

                    Text(
                        modifier = Modifier.clickable {
                            signInNavigation()
                        },
                        text = textBottom2,
                        color = MaterialTheme.colorScheme.primary,
                        fontFamily = FontFamily(
                            Font(
                                R.font.roboto_bold, weight = FontWeight.Bold
                            )
                        ),
                        fontSize = 16.sp
                    )
                }
            }
        }
    }
}
