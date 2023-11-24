package com.example.notweshare.routes

import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.Dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.notweshare.screens.HomeScreen
import com.example.notweshare.screens.NewGroupScreen
import com.example.notweshare.screens.ProfileScreen

//the sealed directive gives control over inheritance
sealed class Screen(val route: String) {
    object HomeScreen : Screen("HomeScreen")
    object NewGroupScreen : Screen("NewScreen")
    object ProfileScreen : Screen("ProfileScreen")
}

@Composable
fun PopulatedNavHost(navController: NavHostController, contentHeight: Dp): Unit {
    NavHost(
        navController,
        startDestination = Screen.HomeScreen.route,
        modifier = androidx.compose.ui.Modifier.height(
            contentHeight
        )
    ) {
        composable(Screen.HomeScreen.route) {
            HomeScreen(navigation = navController, navigateToProfile = {
                navController.navigate(
                    Screen.ProfileScreen.route
                )
            })
        }
        composable(Screen.NewGroupScreen.route) {
            NewGroupScreen(navigateToProfile = {
                navController.navigate(
                    Screen.ProfileScreen.route
                )
            })
        }
        composable(Screen.ProfileScreen.route) { ProfileScreen() }
    }
}