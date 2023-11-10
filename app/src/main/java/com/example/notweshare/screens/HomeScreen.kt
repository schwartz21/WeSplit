package com.example.notweshare.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.example.exampleapplication.routes.Screen

@Composable
fun HomeScreen(navigation: NavController) {
    Column {
        Text(text = "HomeScreen")
        Button(onClick = {navigation.navigate(Screen.NewGroupScreen.route)}) {
            Text(text = "Navigation to the 'NewGroupScreen' screen")
        }
    }
}