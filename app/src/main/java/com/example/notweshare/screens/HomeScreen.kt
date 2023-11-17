package com.example.notweshare.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.exampleapplication.viewmodels.GroupViewModel
import com.example.exampleapplication.viewmodels.UserViewModel
import com.example.notweshare.models.Group
import com.example.notweshare.models.User
import eu.bambooapps.material3.pullrefresh.PullRefreshIndicator
import eu.bambooapps.material3.pullrefresh.pullRefresh
import org.koin.androidx.compose.koinViewModel
import eu.bambooapps.material3.pullrefresh.rememberPullRefreshState
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navigation: NavController,
    modifier: Modifier = Modifier,
    groupViewModel: GroupViewModel = koinViewModel(),
    userViewModel: UserViewModel = koinViewModel(),
) {
    val isRefreshing by remember { mutableStateOf(false) }

    var username by remember { mutableStateOf("") }

    val state = rememberPullRefreshState(refreshing = isRefreshing, onRefresh = {
        groupViewModel.findGroupsWithMember("test")
    })

    when (groupViewModel.isLoading.value) {
        false -> {
            Column {
                Box(modifier = modifier.fillMaxSize()){
//                    LazyColumn(
//                        modifier = Modifier
//                            .fillMaxSize()
//                            .pullRefresh(state)
//                    ) {
//                        items(items = groupViewModel.groups) { group -> GroupCard(group) }
//                    }
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .pullRefresh(state)
                    ) {
                        items(items = userViewModel.users) { user -> UserCard(user) }
                    }
                    PullRefreshIndicator(
                        refreshing = isRefreshing,
                        state = state,
                        modifier = Modifier
                            .align(Alignment.TopCenter)
                    )
                    OutlinedTextField(
                        value = username,
                        onValueChange = { username = it },
                        label = { Text(text = "Username") },
                    )
                    Button(
                        onClick = {
                            login(
                                phoneNumber = "test",
                                password = "test",
                                userViewModel = userViewModel
                            )
                        },
                        modifier = Modifier.align(Alignment.BottomStart)
                    ) {
                        Text(text = "Find User")
                    }
                    Button (
                        onClick = {
                            groupViewModel.postGroup(
                                Group(
                                    name = "Test Group created by button",
                                    expired = false,
                                    members = mutableListOf("test", "test2"),
                                    expenses = mutableListOf(),
                                    createdBy = "test",
                                    createdAt = Date(),
                                )
                            )
                        },
                        modifier = Modifier.align(Alignment.BottomCenter)
                    ) {
                        Text(text = "Create Group")
                    }
                }
            }
        }
        true -> {
            Text(text = "is loading")
        }
    }
}

@Composable
fun GroupCard (group: Group){
    Text(text = group.name ?: "Unknown user")
    Text(text = group.toString() ?: "Unknown amount")
}

@Composable
fun UserCard (user: User){
    Text(text = user.name ?: "Unknown user")
    Text(text = user.phoneNumber ?: "Unknown amount")
}

fun login(
    phoneNumber : String,
    password : String,
    userViewModel: UserViewModel,
) {
    authenticateLoginCredentials(phoneNumber, password, userViewModel)
    // TODO: Navigate to home screen
}

fun authenticateLoginCredentials( phoneNumber: String, password: String, userViewModel: UserViewModel): Boolean {
    // Check if phone number and password are empty
    if (phoneNumber == "" || password == "") {
        println("Phone number or password is incorrect")
        return false
    }

    // Fetch user with phone number
    userViewModel.findUserWithPhoneNumber(phoneNumber)

    // Check if user exists
    if (userViewModel.users.isEmpty()) {
        println("Phone number or password is incorrect")
        return false
    }

    // Check if password is correct
    if (password != userViewModel.users[0].password) {
        println("Phone number or password is incorrect")
        return false
    }

    println("User authenticated")
    return true
}

