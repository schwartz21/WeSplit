package com.example.notweshare.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.exampleapplication.viewmodels.GroupViewModel.Companion.groupViewModel
import com.example.exampleapplication.viewmodels.UserViewModel.Companion.userViewModel
import com.example.notweshare.R
import com.example.notweshare.components.TextFieldCard
import com.example.notweshare.models.Expense
import com.example.notweshare.models.ExpenseMember
import com.example.notweshare.models.User


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun NewExpenseScreen(
    navigateUp: () -> Unit,
) {
    val group = groupViewModel.selectedGroup.value
    val user = userViewModel.activeUser.value

    var errorMessage by remember { mutableStateOf("") }

    var expenseName by remember { mutableStateOf("") }
    var expenseAmount by remember { mutableStateOf("") }

    val selectedUsers = remember {
        mutableStateListOf<String>()
    }

    val largePadding = dimensionResource(R.dimen.padding_large)
    val mediumPadding = dimensionResource(R.dimen.padding_medium)
    val smallPadding = dimensionResource(R.dimen.padding_small)


    LazyColumn(
        modifier = Modifier
            .padding(mediumPadding)
            .fillMaxWidth()
    ) {
        // Icon button navigating back
        // Align button all the way to the left
        item {
            Box() {
                Icon(
                    painter = painterResource(id = R.drawable.backarrow),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier
                        .size(24.dp)
                        .clickable { navigateUp() },
                )

            }
        }
        item {
            //add expense headline
            Text(
                text = "Add expense:",
                style = MaterialTheme.typography.headlineMedium,
            )
            Spacer(modifier = Modifier.padding(smallPadding))
        }
        item {
            //add expense name and expense amount
            expenseName = TextFieldCard(labelValue = "Expense name", input = expenseName)
            Spacer(modifier = Modifier.padding(smallPadding))
            expenseAmount = TextFieldCard(labelValue = "Expense amount", input = expenseAmount)
            Spacer(modifier = Modifier.padding(smallPadding))
        }
        item {
            Text(
                text = "Add group members to expense:",
                style = MaterialTheme.typography.headlineSmall,
            )
            Spacer(modifier = Modifier.padding(smallPadding))
        }
        items(items = group.members) { phoneNumber ->
            val isSelected = selectedUsers.contains(phoneNumber)
            if (phoneNumber != userViewModel.activeUser.value.phoneNumber) {
                ListItem(
                    modifier = Modifier
                        .combinedClickable(
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
                            text = returnNameFromId(phoneNumber),
                        )
                    },
                )
            }
        }
        item {
            Spacer(modifier = Modifier.padding(smallPadding))
            Button(
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                ),
                onClick = {
                    errorMessage = validateExpense(expenseName, expenseAmount, selectedUsers)
                    if (errorMessage == "") {
                        val newExpense =
                            createExpense(expenseName, expenseAmount.toFloat(), selectedUsers, user)
                        groupViewModel.addExpenseToGroup(group.documentID, newExpense)
                        navigateUp()
                    }
                }
            ) {
                Text(
                    text = "Add expense",
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleMedium,
                )
            }
            Text(
                text = errorMessage,
                modifier = Modifier.padding(smallPadding),
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

private fun createExpense(
    expenseName: String,
    expenseAmount: Float,
    selectedUsers: List<String>,
    activeUser: User
): Expense {
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

private fun validateExpense(
    expenseName: String,
    expenseAmount: String,
    selectedUsers: List<String>
): String {
    if (expenseName == "") {
        return "Please fill in expense name"
    }
    if (expenseAmount == "") {
        return "Please fill in expense amount"
    }
    // Check if expense amount is a float with regex
    if (!expenseAmount.matches(Regex("[0-9]+(\\.[0-9]+)?"))) {
        return "Please fill in a valid expense amount"
    }
    if (selectedUsers.isEmpty()) {
        return "Please select at least one group member"
    }
    return ""
}

private fun returnNameFromId(id: String): String {
    var name = "Unknown User"
    userViewModel.users.forEach {
        if (it.documentID == id) {
            name = it.name
        }
    }
    return name
}

