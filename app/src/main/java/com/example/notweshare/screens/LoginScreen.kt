package com.example.notweshare.screens

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.exampleapplication.viewmodels.UserViewModel.Companion.userViewModel
import com.example.notweshare.R
import com.example.notweshare.components.TextFieldCard
import com.example.notweshare.components.passwordTextFieldCard

@Composable
fun LoginScreen(
    navigateToRegister: () -> Unit,
    navigateToMain: () -> Unit,
): Boolean {

    val largePadding = dimensionResource(R.dimen.padding_large)
    val mediumPadding = dimensionResource(R.dimen.padding_medium)
    val smallPadding = dimensionResource(R.dimen.padding_small)

    val questionText = "Not a user? "
    val clickableText = "Register"
    var screenActive = true

    val focusManager = LocalFocusManager.current

    var phoneNumber by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
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
            .pointerInput(Unit) { detectTapGestures(onTap = { focusManager.clearFocus() }) }
            .padding(largePadding),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Login",
                modifier = Modifier
                    .fillMaxWidth(),
                style = MaterialTheme.typography.displayLarge.copy(
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                ),
                color = MaterialTheme.colorScheme.onBackground
            )

//            Spacer(modifier = Modifier.height(10.dp))
                Text(text = errorMessages, color = Color.Red, modifier = Modifier.padding(smallPadding))
                phoneNumber = TextFieldCard("Phone number", phoneNumber)
                password = passwordTextFieldCard("Password", password)
                Spacer(modifier = Modifier.height(10.dp))

                val minHeight = 48.dp

                when (userViewModel.isLoading.value) {
                    true -> {
                        Text(text = "Loading...", color = MaterialTheme.colorScheme.onBackground)
                    }
                    false -> {}
                }

                Button(
                    onClick = {
                        // Set error messages to "" so that it doesn't show the error message from the previous login attempt
                        errorMessages = ""

                        // Check if any of the fields are empty
                        if (phoneNumber == "" || password == "") {
                            errorMessages = "Please fill out all fields"
                            return@Button
                        }

                        userViewModel.findUserWithDocumentID(phoneNumber) { user ->
                            if (user.documentID.isEmpty() || user.password != password) {
                                errorMessages = "Phone number or Password is incorrect"
                            } else if (screenActive) {
                                screenActive = false
                                userViewModel.setTheActiveUser(user)

                                navigateToMain()

                            }
                        }

                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(minHeight),
                    contentPadding = PaddingValues(),
                    shape=RoundedCornerShape(50.dp),
                    colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary)
                ) {
                    Text(
                        text = "Login",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(3.dp))
                ClickableText(
                    modifier = Modifier.fillMaxWidth(),
                    style = MaterialTheme.typography.titleMedium.copy(
                        color = MaterialTheme.colorScheme.onBackground,
                        textAlign = TextAlign.Center),
                    text = annotatedString,
                    onClick = { offset ->
                        annotatedString.getStringAnnotations(offset, offset)
                            .firstOrNull()?.also { navigateToRegister() }
                    })


            }
        }

    return screenActive
}