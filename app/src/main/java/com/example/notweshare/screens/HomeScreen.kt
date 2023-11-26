package com.example.notweshare.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.dp
import com.example.exampleapplication.viewmodels.GroupViewModel
import com.example.exampleapplication.viewmodels.UserViewModel
import com.example.notweshare.R
import com.example.notweshare.components.GroupCard
import com.example.notweshare.models.getDefaultGroup
import eu.bambooapps.material3.pullrefresh.PullRefreshIndicator
import eu.bambooapps.material3.pullrefresh.pullRefresh
import eu.bambooapps.material3.pullrefresh.rememberPullRefreshState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    navigateToGroupDetails: () -> Unit,
    groupViewModel: GroupViewModel,
    userViewModel: UserViewModel,
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
                                userViewModel = userViewModel,
                                groupViewModel = groupViewModel,
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
                    ElevatedButton(
                        onClick = {
                            groupViewModel.postGroup(
                                getDefaultGroup()
                            )
                        },
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .fillMaxWidth(1f)
                            .padding(
                                horizontal = mediumPadding + smallPadding,
                                vertical = smallPadding
                            )
                            .height(50.dp),
                        border = BorderStroke(smallPadding / 4, MaterialTheme.colorScheme.primary),

                        ) {
                        Text(text = "Create Group", style = MaterialTheme.typography.titleMedium)
//                        Text(text = "Create Group")
                    }
                }
            }
        }

        true -> {
            Text(text = "is loading")
        }
    }
}


