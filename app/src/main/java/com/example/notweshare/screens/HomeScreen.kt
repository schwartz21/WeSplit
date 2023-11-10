package com.example.notweshare.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.exampleapplication.viewmodels.UserViewModel
import com.example.notweshare.models.User
import eu.bambooapps.material3.pullrefresh.PullRefreshIndicator
import eu.bambooapps.material3.pullrefresh.pullRefresh
import org.koin.androidx.compose.koinViewModel
import eu.bambooapps.material3.pullrefresh.rememberPullRefreshState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navigation: NavController,
    modifier: Modifier = Modifier,
    userViewModel: UserViewModel = koinViewModel()
) {
    val isRefreshing by remember { mutableStateOf(false) }

    val state = rememberPullRefreshState(refreshing = isRefreshing, onRefresh = {
        userViewModel.findUsers()
    })

    when (userViewModel.isLoading.value) {
        false -> {
            Box(modifier = modifier.fillMaxSize()){
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

            }
        }
        true -> {
            Text(text = "is loading")
        }
    }
}

@Composable
fun UserCard (user: User){
    Text(text = user.name ?: "Unknown user")
}