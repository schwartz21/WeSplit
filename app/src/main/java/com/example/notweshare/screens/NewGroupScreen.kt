package com.example.notweshare.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun NewGroupScreen(navigateToProfile: () -> Unit) {
    Column {
        Text(text = "NewGroupScreen")
        Button(onClick = navigateToProfile) {
            Text(text = "Navigation to the 'ProfileScreen' screen")
        }
    }
}