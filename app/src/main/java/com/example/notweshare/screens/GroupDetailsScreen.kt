package com.example.notweshare.screens

import android.content.Context
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.font.FontWeight
import com.example.exampleapplication.viewmodels.GroupViewModel.Companion.groupViewModel
import com.example.exampleapplication.viewmodels.UserViewModel.Companion.userViewModel
import com.example.notweshare.R
import com.example.notweshare.components.ExpensesCard
import com.example.notweshare.components.GroupCard
import com.example.notweshare.components.GroupDetailsMemberCard
import com.example.notweshare.models.returnNameFromId


@Composable
fun GroupDetailsScreen(
    context: Context,
    navigateToNewExpense: () -> Unit,
) {
    val group = groupViewModel.groups[groupViewModel.selectedGroupIndex.value]

    val largePadding = dimensionResource(R.dimen.padding_large)
    val mediumPadding = dimensionResource(R.dimen.padding_medium)
    val smallPadding = dimensionResource(R.dimen.padding_small)

    LazyColumn() {
        item {
            GroupCard(group)
        }
        item {
            GroupDetailsMemberCard(context, group, userViewModel.activeUser.value.documentID, userViewModel.activeUser.value.name)
            Spacer(modifier = Modifier.padding(smallPadding))
        }
        items(items = group.members) { member ->
            if (member != userViewModel.activeUser.value.documentID){
                GroupDetailsMemberCard(context, group, member, returnNameFromId(member))
                Spacer(modifier = Modifier.padding(smallPadding))
            }
        }
        item {
            Text(
                modifier = Modifier.padding(horizontal = mediumPadding),
                text = "Expenses:",
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.headlineSmall,
            )
            Spacer(modifier = Modifier.padding(smallPadding))
        }
        item {
            Button(
                onClick = { navigateToNewExpense() },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = mediumPadding),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                )
            ) {
                Text(
                    text = "Add new expense",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                )
            }
            Spacer(modifier = Modifier.padding(smallPadding))
        }
        items(items = group.expenses) { expense ->
            ExpensesCard(expense)
            Spacer(modifier = Modifier.padding(smallPadding))
        }
    }
}