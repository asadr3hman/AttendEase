package com.example.attendancemanage.ui.components

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.attendancemanage.R


val textFieldPadding = 32.dp
val cornerRadius = 25.dp


@Composable
fun ScreenTextFeild(
    modifier: Modifier,
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
        modifier = modifier
            .fillMaxWidth()
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
fun Devider() {

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar() {
    var searchQuery by remember { mutableStateOf("") }

    TopAppBar(
        title = {
            BasicTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Search
                ),
                keyboardActions = KeyboardActions(
                    onSearch = {
                        // Handle search action
                    }
                ),
                singleLine = true
            )
        },
        actions = {
            IconButton(onClick = {
                // Handle search button click
            }) {
                Icon(imageVector = Icons.Default.Search, contentDescription = "Search")
            }
        }
    )
}

@Composable
fun ItemList() {
    LazyColumn {
        items(10) { index ->
            ListItem(
                headlineContent = { Text("Two line list item with trailing") },
                supportingContent = { Text("Secondary text") },
                trailingContent = { Text("meta") },
                leadingContent = {
                    Icon(
                        Icons.Filled.Favorite,
                        contentDescription = "Localized description",
                    )
                }
            )
            Devider()
        }
    }
}