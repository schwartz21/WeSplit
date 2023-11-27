package com.example.notweshare.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.exampleapplication.viewmodels.UserViewModel.Companion.userViewModel
import com.example.notweshare.R
import com.example.notweshare.components.passwordTextFieldCard
import com.example.notweshare.components.TextFieldCard
import com.example.notweshare.models.User

@Composable
fun RegisterScreen(
    navigateToHomeScreen: () -> Unit,
    navigateToLogin: () -> Unit,
) {
    val questionText = "Already a user? "
    val clickableText = "Login"
    val focusManager = LocalFocusManager.current

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

    val largePadding = dimensionResource(R.dimen.padding_large)

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) { detectTapGestures(onTap = { focusManager.clearFocus() }) }
            .padding(largePadding),
        color=MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Register",
                modifier = Modifier
                    .fillMaxWidth(),
                style = TextStyle(
                    fontSize = 48.sp,
                    fontWeight = FontWeight.SemiBold,
                    fontStyle = FontStyle.Normal,
                    textAlign = TextAlign.Center
                ),
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(10.dp))
            fullName = TextFieldCard("Full name...", "")
            phoneNumber = TextFieldCard("Phone number...", "")
            email = TextFieldCard("Email...", "")
            password = passwordTextFieldCard("Password...", "")
            confirmPassword = passwordTextFieldCard("Confirm password...", "")
            Spacer(modifier = Modifier.height(1.dp))
            ClickableText(
                modifier = Modifier.fillMaxWidth(),
                style = MaterialTheme.typography.titleMedium.copy(
                    color = MaterialTheme.colorScheme.onBackground,
                    textAlign = TextAlign.Center
                ),
                text = annotatedString, onClick = { offset ->
                    annotatedString.getStringAnnotations(offset, offset)
                        .firstOrNull()?.also { navigateToLogin() }
                })

            Spacer(modifier = Modifier.height(10.dp))
            val minHeight = 48.dp

            Button(
                onClick = {
                    if (password != confirmPassword) {
                        errorMessages = "Passwords do not match"
                        return@Button
                    } else if (fullName == "" || phoneNumber == "" || email == "" || password == "") {
                        errorMessages = "Please fill in all fields"
                        return@Button
                    } else if (!phoneNumber.matches(Regex("[0-9]+"))) {
                        errorMessages = "Phone number can only contain numbers"
                        return@Button
                    }
                    userViewModel.findUserWithDocumentID(phoneNumber) { user ->
                        if (user.documentID == phoneNumber) {
                            errorMessages = "User already exists"
                        } else {
                            registerNewUser(fullName, phoneNumber, email, password)
                            navigateToHomeScreen()
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(minHeight),
                contentPadding = PaddingValues(),
                colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary),
                shape = RoundedCornerShape(50.dp)
            ) {
                Text(
                    text = "Register",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Text(text = errorMessages, color = Color.Red, modifier = Modifier.padding(10.dp))
        }
    }
}

private fun registerNewUser(
    fullName: String,
    phoneNumber: String,
    email: String,
    password: String
) {
    val user = User(fullName, phoneNumber, email, password, documentID = phoneNumber)
    userViewModel.postUser(user)
    userViewModel.setTheActiveUser(user)
}