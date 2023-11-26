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
            phoneNumber = textFieldComponent("Phone number", phoneNumber)
            password = passwordTextFieldComponent("Password", password)
            Spacer(modifier = Modifier.height(1.dp))
            ClickableText(text = annotatedString, onClick = { offset ->
                annotatedString.getStringAnnotations(offset, offset)
                    .firstOrNull()?.also { navigateToRegister() }
            })

            Spacer(modifier = Modifier.height(10.dp))
            val minHeight = 48.dp

            Button(
                onClick = {
                    if (validateLogin(phoneNumber, password, userViewModel)) {
                        groupViewModel.findGroupsWithMember(phoneNumber)
                        navigateToHomeScreen()
                    } else {
                        // Display that password or phone number is incorrect
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
        }
    }
}

private fun validateLogin(phoneNumber: String, password: String, userViewModel: UserViewModel): Boolean {
    var ids: MutableList<String> = mutableListOf()
    ids.add(phoneNumber)

    if (phoneNumber == "" || password == "") {
        println("Returning false")
        return false
    }

    userViewModel.findUsersWithDocumentIDs(ids)
    println("Fetching users...")
    println("Users right after fetch: ")
    println(userViewModel.users)

    if (userViewModel.users.isEmpty()) {
        println("Returning false with users:")
        println(userViewModel.users)
        return false
    }

    if (userViewModel.users[0].password != password) {
        println("Returning false because of password mismatch")
        println("Password needed to be " + userViewModel.users[0].password + " and password was " + password)
        return false
    }

    userViewModel.setTheActiveUser(userViewModel.users[0])

    return true
}