package com.example.notweshare

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.exampleapplication.routes.Screen
import com.example.notweshare.screens.HomeScreen
import com.example.notweshare.screens.NewGroupScreen
import com.example.notweshare.screens.ProfileScreen
import com.example.notweshare.ui.theme.NotWeShareTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NotWeShareTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    Navigation("for the Android 2")
                }
            }
        }
    }
}

@Composable
fun Navigation(name: String, modifier: Modifier = Modifier) {

    val navigation = rememberNavController();

    Column {
        NavHost(
            navController = navigation,
            startDestination = Screen.HomeScreen.route
        ){
            composable(Screen.HomeScreen.route) { HomeScreen(navigation = navigation) }
            composable(Screen.NewGroupScreen.route) { NewGroupScreen(navigation = navigation)}
            composable(Screen.ProfileScreen.route) { ProfileScreen(navigation = navigation)}
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    NotWeShareTheme {
        Navigation("for the Android")
    }
}


//private fun fetchUsers() {
//    FetchUsers.findUsers(FirestoreQueries.UserQueries.userWithPhoneNumber("20838848")) { userArray ->
//        if (userArray.isNotEmpty()) {
//            print(userArray.first().name)
//        }
//    }
//}