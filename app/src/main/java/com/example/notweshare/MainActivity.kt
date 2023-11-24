package com.example.notweshare

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.exampleapplication.routes.Screen
import com.example.notweshare.notification.Counter
import com.example.notweshare.notification.NotificationService
import com.example.notweshare.screens.HomeScreen
import com.example.notweshare.screens.NewGroupScreen
import com.example.notweshare.screens.ProfileScreen
import com.example.notweshare.ui.theme.NotWeShareTheme

import android.util.Log


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val service = NotificationService(applicationContext)
        setContent {
            NotWeShareTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    Navigation("for the Android 2")

                    //For notification
                    //
                    Button(onClick = { service.showNotification(Counter.value)})
                    {
                         Text(text = "Show notification")
                    }
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