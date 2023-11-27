package com.example.notweshare.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import com.example.exampleapplication.viewmodels.GroupViewModel.Companion.groupViewModel
import com.example.notweshare.R
import com.example.notweshare.components.GroupCard
import eu.bambooapps.material3.pullrefresh.PullRefreshIndicator
import eu.bambooapps.material3.pullrefresh.pullRefresh
import eu.bambooapps.material3.pullrefresh.rememberPullRefreshState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    navigateToGroupDetails: () -> Unit,
) {
    val isRefreshing by remember { mutableStateOf(false) }

    val state = rememberPullRefreshState(refreshing = isRefreshing, onRefresh = {
        groupViewModel.findGroupsWithMember("test")
    })

    val mediumPadding = dimensionResource(R.dimen.padding_medium)
    val smallPadding = dimensionResource(R.dimen.padding_small)


    when (groupViewModel.isLoading.value) {
        false -> {
            Column {
                Box(
                    modifier = modifier
                        .fillMaxSize()
                ) {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .pullRefresh(state)
                    ) {
                        items(items = groupViewModel.groups) { group ->
                            GroupCard(
                                group,
                                onNavigateToGroupDetails = navigateToGroupDetails,
                            )
                        }
                    }
                    PullRefreshIndicator(
                        refreshing = isRefreshing,
                        state = state,
                        modifier = Modifier
                            .align(Alignment.TopCenter)
                    )
                }
            }
        }

        true -> {
            Text(text = "is loading")
        }
    }
}


