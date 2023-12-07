package com.example.notweshare

import com.example.notweshare.notification.NotificationJobIntentService
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.notweshare.models.TabItem
import com.example.notweshare.screens.HomeScreen
import com.example.notweshare.screens.NewGroupScreen
import com.example.notweshare.screens.PermissionDialog
import com.example.notweshare.screens.ProfileScreen
import com.example.notweshare.ui.theme.NotWeShareTheme
import com.google.firebase.FirebaseApp
import com.google.firebase.messaging.FirebaseMessaging

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        getFireBaseToken()
        setContent {
            NotWeShareTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    Navigation("for the Android 2")
//                    Button(onClick= {startNotificationService()}){
//                        Text(text = "CLICK ME")
//                    }
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
                        PermissionDialog()
                    }
                }
            }
        }
    }

    private fun getFireBaseToken() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                //save this token to the user and store in DB for late use.
                //dont know if we should check if the token is the same as previous every time the app launches
                val token = task.result
                Log.d("NotificationService", token)
                println(token)
                // Send this token to your server to identify and target specific devices
            } else {
                Log.e("FCM Token", "Failed to get token: ${task.exception}")
            }
        }
    }
    private fun startNotificationService() {
        // Create an intent to start the com.example.notweshare.notification.NotificationJobIntentService
        val intent = Intent(this, NotificationJobIntentService::class.java)
        intent.putExtra("title", "Notification Title")
        intent.putExtra("body", "Notification Body")

        // Enqueue the work to be done by the service
        NotificationJobIntentService.enqueueWork(this, intent)
    }
}

@Composable
fun Navigation(name: String, modifier: Modifier = Modifier) {



    val navController = rememberNavController()
    var selectedTabIndex by remember { mutableStateOf(0) }
    val tabs = listOf(
        TabItem("Home", R.drawable.home),
        TabItem("Groups", R.drawable.group),
        TabItem("Profile", R.drawable.profile)
    )
    Scaffold(
        bottomBar = {
            BottomAppBar(
                contentColor = Color.White,
                modifier = Modifier.fillMaxWidth()
            ) {
                tabs.forEachIndexed { index, tab ->
                    val tint = ColorFilter.tint(if (selectedTabIndex == index) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onBackground)
                    IconButton(
                        onClick = {
                            selectedTabIndex = index
                            navController.navigate(index.toString())
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Bottom
                        ) {
                            Image(
                                colorFilter = tint,
                                painter = painterResource(tab.icon),
                                contentDescription = null,
                                modifier = Modifier.size(24.dp)
                            )
                            Text(
                                text = tab.title,
                                style = MaterialTheme.typography.bodySmall,
                                color = if (selectedTabIndex == index) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onBackground,
                            )
                        }
                    }
                }
            }
        }
    ) {
        NavHost(navController, startDestination = "0") {
            composable("0") { HomeScreen(navigation = navController) }
            composable("1") { NewGroupScreen(navigation = navController) }
            composable("2") { ProfileScreen(navigation = navController) }
        }
        // THIS FOLLOWING LINE MUST BE THERE FOR THE LINTER TO WORK
        val something = it
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    NotWeShareTheme {
        Navigation("for the Android")
    }
}
