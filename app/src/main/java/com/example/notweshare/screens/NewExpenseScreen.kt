package com.example.notweshare.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.exampleapplication.viewmodels.GroupViewModel
import com.example.exampleapplication.viewmodels.UserViewModel
import com.example.notweshare.models.Expense
import com.example.notweshare.models.ExpenseMember
import com.example.notweshare.models.User


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun NewExpenseScreen(
    navigateUp: () -> Unit,
    groupViewModel: GroupViewModel,
    userViewModel: UserViewModel,
) {
    val group = groupViewModel.selectedGroup.value
    val user = userViewModel.activeUser.value

    var expenseName by remember { mutableStateOf("") }
    var expenseAmount by remember { mutableStateOf("") }

    val selectedUsers = remember {
        mutableStateListOf<String>()
    }

    LazyColumn(
        modifier = Modifier
            .padding(36.dp)
            .fillMaxWidth()
    ) {
        item {
            //add expense headline
            Text(
                text = "Add expense:",
                style = MaterialTheme.typography.headlineMedium,
            )
            Spacer(modifier = Modifier.padding(10.dp))
        }
        item {
            //add expense name and expense amount
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = expenseName,
                onValueChange = { expenseName = it },
                label = { Text(text = "Expense name") },
            )
            Spacer(modifier = Modifier.padding(10.dp))
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = expenseAmount,
                onValueChange = { value ->
                    expenseAmount = value.filter { it.isDigit() }
                },
                label = { Text(text = "Expense amount") },
            )
            Spacer(modifier = Modifier.padding(10.dp))
        }
        item {
            //add group members headline
            Text(
                text = "Add group members to expense:",
                style = MaterialTheme.typography.headlineMedium,
            )
            Spacer(modifier = Modifier.padding(10.dp))
        }
        items(items = group.members) { phoneNumber ->
            val isSelected = selectedUsers.contains(phoneNumber)
            if (phoneNumber != userViewModel.activeUser.value.phoneNumber) {
                ListItem(
                    modifier = Modifier.combinedClickable(
                        onClick = {
                            if (isSelected) {
                                selectedUsers.remove(phoneNumber)
                            } else {
                                selectedUsers.add(phoneNumber)
                            }
                        }
                    ),
                    leadingContent = {
                        if (isSelected) {
                            Icon(
                                imageVector = Icons.Rounded.CheckCircle,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Rounded.CheckCircle,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.outline,
                            )
                        }
                    },
                    headlineContent = {
                        Text(
                            text = phoneNumber,
                        )
                    },
                )
            }
        }
        item {
            Spacer(modifier = Modifier.padding(10.dp))
            //add expense button
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    group.expenses.add(createExpense(expenseName, expenseAmount.toFloat(), selectedUsers, user))
                    groupViewModel.addExpenseToGroup(group.documentID, group.expenses)
                    navigateUp()
                }
            ) {
                Text(text = "Add expense")
            }
        }
    }
}

fun createExpense(expenseName: String, expenseAmount: Float, selectedUsers: List<String>, activeUser: User): Expense {
    var expenseMembers = mutableMapOf<String, ExpenseMember>()
    var i = 0
    selectedUsers.forEach {
        expenseMembers[i.toString()] = ExpenseMember(selectedUsers[i], false)
        i++
    }
    expenseMembers[i.toString()] = ExpenseMember(activeUser.documentID, true)

    var expense = Expense(expenseName, expenseAmount, expenseMembers, activeUser.documentID)

    return expense
}