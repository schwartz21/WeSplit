package com.example.notweshare.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.example.exampleapplication.routes.Screen
import com.example.exampleapplication.routes.ScreenOptions

@Composable
fun NewGroupScreen(navigation: NavController) {
    Column {
        Text(text = "NewGroupScreen")
        Button(onClick = {navigation.navigate(ScreenOptions.ProfileScreen.name)}) {
            Text(text = "Navigation to the 'ProfileScreen' screen")
        }
    }
}