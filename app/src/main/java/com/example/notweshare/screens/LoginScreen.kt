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
import com.example.notweshare.components.passwordTextFieldCard
import com.example.notweshare.components.textFieldCard
import kotlinx.coroutines.runBlocking

@Composable
fun LoginScreen(
    navigateToRegister: () -> Unit,
    navigateToHomeScreen: () -> Unit,
    groupViewModel: GroupViewModel,
    userViewModel: UserViewModel,
    ) {
    val questionText = "Not a user? "
    val clickableText = "Register"

    var phoneNumber by remember { mutableStateOf("")}
    var password by remember { mutableStateOf("")}
    var errorMessages by remember { mutableStateOf("") }


    println("phone number is $phoneNumber and password is $password")

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
                text = "Login",
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
            phoneNumber = textFieldCard("Phone number", phoneNumber)
            password = passwordTextFieldCard("Password", password)
            Spacer(modifier = Modifier.height(1.dp))
            ClickableText(text = annotatedString, onClick = { offset ->
                annotatedString.getStringAnnotations(offset, offset)
                    .firstOrNull()?.also { navigateToRegister() }
            })

            Spacer(modifier = Modifier.height(10.dp))
            val minHeight = 48.dp

            Button(
                onClick = {
                    errorMessages = validateLogin(phoneNumber, password, userViewModel)
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
                        text = "Login",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            Text(text = errorMessages, color = Color.Red, modifier = Modifier.padding(10.dp))

        }
    }
}

private fun validateLogin(phoneNumber: String, password: String, userViewModel: UserViewModel): String {

    if (phoneNumber == "" || password == "") {
        return "Please fill out all fields"
    }

    // Fetch user with id and then check stuff
    userViewModel.findUserWithDocumentID(phoneNumber)

//    while (userViewModel.users.isEmpty()) {
//        println("Waiting for users to be fetched...")
//        runBlocking {
//            kotlinx.coroutines.delay(1000)
//        }
//    }

    println("User is ${userViewModel.users[0]}")

    if (userViewModel.users[0].documentID == "g") {
        return "Password or Phone number is incorrect"
    }

    if (userViewModel.users[0].password != password) {
        return "Password or Phone number is incorrect"
    }

    userViewModel.setTheActiveUser(userViewModel.users[0])

    return ""
}