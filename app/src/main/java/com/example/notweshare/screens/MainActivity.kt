package com.example.notweshare.screens

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.notweshare.backend.FetchUsers
import com.example.notweshare.backend.FirestoreQueries
import com.example.notweshare.models.User
import com.example.notweshare.ui.theme.NotWeShareTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NotWeShareTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Greeting("Android")
                }
            }
        }

        this.fetchUsers()
    }

    private fun fetchUsers() {
        FetchUsers.findUsers(FirestoreQueries.UserQueries.userWithPhoneNumber("20838848")) { userArray ->
            if (userArray.isNotEmpty()) {
                println(userArray.first().name)
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    NotWeShareTheme {
        Greeting("Android")
    }
}