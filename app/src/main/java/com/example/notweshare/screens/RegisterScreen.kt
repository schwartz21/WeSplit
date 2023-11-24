package com.example.notweshare.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.notweshare.ui.theme.backgroundColor
import com.example.notweshare.ui.theme.highlightColor
import com.example.notweshare.ui.theme.textColor
import com.example.notweshare.components.ButtonComponent
import com.example.notweshare.components.PasswordTextFieldComponent
import com.example.notweshare.components.TextFieldComponent

@Preview
@Composable
fun RegisterScreen() {
    val questionText = "Already a user? "
    val clickableText = "Login"

    val annotatedString = buildAnnotatedString {
        append(questionText)
        withStyle(style = SpanStyle(color = highlightColor)) {
            pushStringAnnotation(tag = clickableText, annotation = clickableText)
            append(clickableText)
        }
    }

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
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
                color = textColor
            )

            Spacer(modifier = Modifier.height(10.dp))
            TextFieldComponent("Full name...")
            TextFieldComponent("Phone number...")
            TextFieldComponent("Email...")
            PasswordTextFieldComponent("Password...")
            PasswordTextFieldComponent("Confirm password...")
            Spacer(modifier = Modifier.height(1.dp))
            ClickableText(text = annotatedString, onClick = { offset ->
                annotatedString.getStringAnnotations(offset, offset)
                    .firstOrNull()?.also { span ->
                        Log.d("ClickableText", "{$span}")
                    }
            })

            Spacer(modifier = Modifier.height(10.dp))
            ButtonComponent(textValue = "Register")
        }
    }
}
