package com.example.attendancemanage.ui.components

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.Visibility
import com.example.attendancemanage.R
import com.example.attendancemanage.ui.auth.validateSignIn
import com.example.attendancemanage.ui.auth.validateSignUp
import com.example.attendancemanage.ui.theme.AttendanceManageTheme


val textFieldPadding = 32.dp
val cornerRadius = 25.dp




@Composable
fun ScreenTextFeild(
    text: String,
    hint: String,
    leadingIcon: ImageVector,
    password: Boolean,
    onText: (String) -> Unit,
) {
    var passwordHidden by rememberSaveable { mutableStateOf(true) }
    OutlinedTextField(
        visualTransformation = if (password) {
            if (passwordHidden) PasswordVisualTransformation() else VisualTransformation.None
        } else VisualTransformation.None,
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                start = textFieldPadding,
                end = textFieldPadding,
                top = textFieldPadding,
            )
            .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(cornerRadius)),
        value = text,
        colors = OutlinedTextFieldDefaults.colors(
            cursorColor = MaterialTheme.colorScheme.primary,
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            unfocusedBorderColor = Color.Transparent,
        ),
        onValueChange = { onText(it) },
        singleLine = true,
        shape = RoundedCornerShape(cornerRadius),
        textStyle = screenTextField(MaterialTheme.colorScheme.primary),
        placeholder = {
            Text(
                text = hint,
                style = screenTextField(Color(0xFF808080))
            )
        },
        leadingIcon = {
            Icon(
                imageVector = leadingIcon,
                contentDescription = hint,
                tint = MaterialTheme.colorScheme.primary
            )
        },
        keyboardOptions =
        KeyboardOptions(
            keyboardType = if (password) {
                KeyboardType.Password
            } else KeyboardType.Text
        ),

        trailingIcon = {
            if (password) {
                IconButton(onClick = { passwordHidden = !passwordHidden }) {
                    val visibilityIcon =
                        if (passwordHidden) Icons.Filled.ArrowBack else Icons.Filled.ArrowForward
                    val description = if (passwordHidden) "Show password" else "Hide password"
                    Icon(imageVector = visibilityIcon, contentDescription = description)
                }
            }
        }
    )


}

@Composable
fun screenTextField(textColor: Color) = androidx.compose.ui.text.TextStyle(
    fontFamily = FontFamily(Font(R.font.roboto)),
    letterSpacing = 1.sp,
    color = textColor
)

fun showToast(context: Context, message: String) {
    Toast.makeText(
        context.applicationContext, message,
        Toast.LENGTH_SHORT
    ).show()
}
@Composable
fun Devider(){

}