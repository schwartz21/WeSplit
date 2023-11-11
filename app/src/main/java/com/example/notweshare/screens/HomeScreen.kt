package com.example.notweshare.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.ui.unit.Dp
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
) {
    val isRefreshing by remember { mutableStateOf(false) }

    val state = rememberPullRefreshState(refreshing = isRefreshing, onRefresh = {
        groupViewModel.findGroupsWithMember("test")
    })

    when (groupViewModel.isLoading.value) {
        false -> {
            Column {
                Box(modifier = modifier.fillMaxSize()){
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .pullRefresh(state)
                    ) {
                        items(items = groupViewModel.groups) { group -> GroupCard(group) }
                    }
                    PullRefreshIndicator(
                        refreshing = isRefreshing,
                        state = state,
                        modifier = Modifier
                            .align(Alignment.TopCenter)
                    )
                    Button (
                        onClick = {
                            groupViewModel.postGroup(
                                Group(
                                    name = "Test Group",
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