package com.example.notweshare.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.exampleapplication.viewmodels.GroupViewModel
import com.example.exampleapplication.viewmodels.UserViewModel
import com.example.notweshare.components.passwordTextFieldComponent
import com.example.notweshare.components.textFieldComponent
import com.example.notweshare.models.User

@Composable
fun RegisterScreen(
    navigateToHomeScreen: () -> Unit,
    navigateToLogin: () -> Unit,
    groupViewModel: GroupViewModel,
    userViewModel: UserViewModel,
) {
    val questionText = "Already a user? "
    val clickableText = "Login"

    var fullName by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var errorMessages by remember { mutableStateOf("") }

    val annotatedString = buildAnnotatedString {
        append(questionText)
        withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.primary)) {
            pushStringAnnotation(tag = clickableText, annotation = clickableText)
            append(clickableText)
        }
    }

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(28.dp)
    ) {
        Column(modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Register",
                modifier = Modifier
                    .fillMaxWidth(),
                style = TextStyle (
                    fontSize = 48.sp,
                    fontWeight = FontWeight.SemiBold,
                    fontStyle = FontStyle.Normal,
                    textAlign = TextAlign.Center
                ),
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(10.dp))
            fullName = textFieldComponent("Full name...", "")
            phoneNumber = textFieldComponent("Phone number...", "")
            email = textFieldComponent("Email...", "")
            password = passwordTextFieldComponent("Password...", "")
            confirmPassword = passwordTextFieldComponent("Confirm password...", "")
            Spacer(modifier = Modifier.height(1.dp))
            ClickableText(text = annotatedString, onClick = { offset ->
                annotatedString.getStringAnnotations(offset, offset)
                    .firstOrNull()?.also { navigateToLogin() }
            })

            Spacer(modifier = Modifier.height(10.dp))
            val minHeight = 48.dp

            Button(
                onClick = {
                    errorMessages = validateAndRegister(fullName, phoneNumber, email, password, confirmPassword, userViewModel)
                    if (errorMessages == "") {
                        groupViewModel.findGroupsWithMember(phoneNumber)
                        navigateToHomeScreen()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(minHeight),
                contentPadding = PaddingValues(),
                colors = ButtonDefaults.buttonColors(Color.Transparent)
            ) {

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(minHeight)
                        .background(
                            color = MaterialTheme.colorScheme.primary,
                            shape = RoundedCornerShape(50.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Register",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Text(text = errorMessages, color = Color.Red, modifier = Modifier.padding(10.dp))
        }
    }
}

/**
 * Validates the user input and registers the user if the input is valid
 * @param fullName The full name of the user
 * @param phoneNumber The phone number of the user
 * @param email The email of the user
 * @param password The password of the user
 * @param confirmPassword The password confirmation of the user
 * @param userViewModel The user view model
 * @return An error message if the input is invalid, otherwise an empty string
 */
private fun validateAndRegister(fullName: String, phoneNumber: String, email: String, password: String, confirmPassword: String, userViewModel: UserViewModel): String {
    if (fullName == "" || phoneNumber == "" || email == "" || password == "" || confirmPassword == "") {

        return "Please fill in all fields"
    }

    // Check if phone number consists only of numbers
    if (!phoneNumber.matches(Regex("[0-9]+"))) {
        return "Phone number can only contain numbers"
    }

    if (password != confirmPassword) {
        return "Passwords do not match"
    }

    userViewModel.findUserWithDocumentID(phoneNumber)
    if (userViewModel.users.isNotEmpty()) {
        return "User already exists"
    }

    registerNewUser(fullName, phoneNumber, email, password, userViewModel)

    return ""
}

private fun registerNewUser(fullName: String, phoneNumber: String, email: String, password: String, userViewModel: UserViewModel) {
    val user = User(fullName, phoneNumber, email, password, documentID = phoneNumber)
    userViewModel.postUser(user)
    userViewModel.setTheActiveUser(user)
}