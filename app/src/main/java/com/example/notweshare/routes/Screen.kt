package com.example.exampleapplication.routes

//the sealed directive gives control over inheritance
sealed class Screen(val route: String) {
    object HomeScreen : Screen(ScreenOptions.HomeScreen.name)
    object NewGroupScreen: Screen(ScreenOptions.NewGroupScreen.name)
    object ProfileScreen: Screen(ScreenOptions.ProfileScreen.name)
}

enum class ScreenOptions(string: String){
    HomeScreen("home"),
    NewGroupScreen("newGroup"),
    ProfileScreen("Profile")
}