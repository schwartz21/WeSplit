package com.example.notweshare.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.font.FontWeight
import com.example.exampleapplication.viewmodels.GroupViewModel
import com.example.exampleapplication.viewmodels.UserViewModel
import com.example.notweshare.R
import com.example.notweshare.models.Group
import kotlinx.coroutines.flow.MutableStateFlow
import java.util.Date

@Composable
fun NewGroupScreen(
    navigateToGroups: () -> Unit,
    groupViewModel: GroupViewModel,
    userViewModel: UserViewModel
) {
    var groupName by remember { mutableStateOf("") }
    var addMemberByPhoneNumber by remember { mutableStateOf("") }

    val _memberList = remember { MutableStateFlow(listOf<String>()) }
    val memberList by remember { _memberList }.collectAsState()

    var errorAddGroupMemberMessage by remember { mutableStateOf("") }
    var errorAddGroupMessage by remember { mutableStateOf("") }

    val largePadding = dimensionResource(R.dimen.padding_large)
    val mediumPadding = dimensionResource(R.dimen.padding_medium)
    val smallPadding = dimensionResource(R.dimen.padding_small)

    // Add members to a member list
    fun addItem(item: String): String {
        val newList = ArrayList(memberList)
        if (!newList.contains(item)) {
            newList.add(item)
        } else {
            return "This member is already added."
        }
        _memberList.value = newList
        addMemberByPhoneNumber = ""
        return ""
    }

    Column(
        modifier = Modifier
            .padding(largePadding)
            .fillMaxWidth(),
    ) {
        Text(
            text = "Create a new group",
            style = MaterialTheme.typography.headlineLarge,
        )
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = groupName,
            onValueChange = { groupName = it },
            label = { Text(text = "Group name") },
        )
        Spacer(modifier = Modifier.padding(smallPadding))
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = addMemberByPhoneNumber,
            onValueChange = { value ->
                addMemberByPhoneNumber = value.filter { it.isDigit() }
            },
            label = { Text(text = "Add member by phone number") },
        )
        if (errorAddGroupMemberMessage.isNotEmpty()) {
            Text(
                text = errorAddGroupMemberMessage,
                color = MaterialTheme.colorScheme.error,
            )
        }
        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                if (validatePhoneNumber(addMemberByPhoneNumber)) {
                    errorAddGroupMemberMessage = addItem(addMemberByPhoneNumber)
                } else {
                    return@Button
                }
            }) {
            Text(
                text = "Add member",
                style = MaterialTheme.typography.titleMedium,
            )
        }
        Spacer(modifier = Modifier.padding(smallPadding))
        LazyColumn(content = {
            item(key = "memberList") {
                Text(
                    text = "Members currently added:",
                    style = MaterialTheme.typography.headlineSmall
                )
            }
            items(memberList) { item ->
                Text(text = item)
            }
        })
        Spacer(modifier = Modifier.padding(smallPadding))
        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                if (validateGroup(groupName, memberList).isEmpty()) {
                    createGroup(
                        groupName,
                        memberList.toMutableList(),
                        groupViewModel,
                        userViewModel
                    )
                    navigateToGroups()
                } else {
                    errorAddGroupMessage = validateGroup(groupName, memberList)
                }
            }
        ) {
            Text(
                text = "Create the new group",
                style = MaterialTheme.typography.titleMedium,
            )
        }
        Text(
            text = errorAddGroupMessage,
            color = MaterialTheme.colorScheme.error,
            fontWeight = FontWeight.Bold,
        )
    }
}

private fun createGroup(
    groupName: String,
    memberList: MutableList<String>,
    groupViewModel: GroupViewModel,
    userViewModel: UserViewModel
) {
    val newMemberList = memberList
    newMemberList.add(userViewModel.activeUser.value.phoneNumber)
    val newGroup = Group()
    newGroup.name = groupName
    newGroup.members = newMemberList
    newGroup.createdBy = userViewModel.activeUser.value.documentID
    groupViewModel.postGroup(newGroup)
}

// -- For later --
// Validate if the phone number exists in the database
private fun validatePhoneNumber(phoneNumber: String): Boolean {
    return phoneNumber.isNotEmpty()
}

private fun validateGroupName(groupName: String): Boolean {
    return groupName.isNotEmpty()
}

private fun validateGroup(groupName: String, memberList: List<String>): String {
    if (!validateGroupName(groupName)) {
        return "Invalid group name."
    }
    if (memberList.isEmpty()) {
        return "No members added to the group."
    }
    return ""
}