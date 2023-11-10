package com.example.exampleapplication.routes

//the sealed directive gives control over inheritance
sealed class Screen(val route: String) {
    object HomeScreen : Screen("home-screen")
    object NewGroupScreen: Screen("new-group-screen")
    object ProfileScreen: Screen("profile-screen")
}