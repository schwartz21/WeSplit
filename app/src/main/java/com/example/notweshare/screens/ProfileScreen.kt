package com.example.notweshare.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.BlendMode
import androidx.navigation.NavController
import com.example.exampleapplication.routes.Screen

@Composable
fun ProfileScreen(navigation: NavController) {
    Column {
        Text(text = "ProfileScreen")
        Button(onClick = {navigation.navigate(Screen.HomeScreen.route)}) {
            Text(text = "Navigation to the 'HomeScreen' screen")
        }
    }
}