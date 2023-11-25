package com.example.notweshare.screens

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.exampleapplication.viewmodels.GroupViewModel
import com.example.exampleapplication.viewmodels.UserViewModel
import com.example.notweshare.components.ExpensesCard
import com.example.notweshare.components.GroupCard
import com.example.notweshare.components.GroupDetailsMemberCard


@Composable
fun GroupDetailsScreen(
    navigateToNewExpense: () -> Unit,
    groupViewModel: GroupViewModel,
    userViewModel: UserViewModel,
) {
    val group = groupViewModel.selectedGroup.value
    LazyColumn(modifier = Modifier.padding(10.dp)) {
        item {
            GroupCard(group, userViewModel = userViewModel,groupViewModel = groupViewModel)
        }
        items(items = group.members){
            member -> GroupDetailsMemberCard(member)
        }
        item {
            Text(
                text = "Expenses:",
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.headlineMedium,
            )
            Spacer(modifier = Modifier.padding(5.dp))
        }
        item {
            Button(
                onClick = { navigateToNewExpense() },
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(
                    text = "Add new expense",
                    fontSize = 14.sp,
                )
            }
            Spacer(modifier = Modifier.padding(5.dp))
        }
        items(items = group.expenses){
            expense -> ExpensesCard(expense)
        }
    }
}
