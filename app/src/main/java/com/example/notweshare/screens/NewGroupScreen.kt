package com.example.notweshare.screens

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.font.FontWeight
import com.example.exampleapplication.viewmodels.GroupViewModel.Companion.groupViewModel
import com.example.exampleapplication.viewmodels.UserViewModel.Companion.userViewModel
import com.example.notweshare.R
import com.example.notweshare.components.MemberCard
import com.example.notweshare.components.TextFieldCard
import com.example.notweshare.models.Group
import com.example.notweshare.models.returnNameFromId

@Composable
fun NewGroupScreen(
    navigateToGroups: () -> Unit,
) {
    var groupName by remember { mutableStateOf("") }
    var addMemberByPhoneNumber by remember { mutableStateOf("") }

    val memberList = remember { mutableStateListOf<String>() }

    var errorAddGroupMemberMessage by remember { mutableStateOf("") }
    var errorAddGroupMessage by remember { mutableStateOf("") }

    val focusManager = LocalFocusManager.current

    val largePadding = dimensionResource(R.dimen.padding_large)
    val mediumPadding = dimensionResource(R.dimen.padding_medium)
    val smallPadding = dimensionResource(R.dimen.padding_small)

    // Add members to a member list
    fun addItem(item: String) {
        errorAddGroupMemberMessage = ""
        if (userViewModel.activeUser.value.documentID.equals(item)) {
            errorAddGroupMemberMessage = "You are automatically added to the group."
            addMemberByPhoneNumber = ""
            return
        }
        userViewModel.findUserWithDocumentID(item) { user ->
            if (user.documentID.isNotEmpty()) {
                if (!memberList.contains(item)) {
                    memberList.add(item)
                    addMemberByPhoneNumber = ""
                } else {
                    errorAddGroupMemberMessage = "This member is already added."
                    addMemberByPhoneNumber = ""
                }
            } else {
                errorAddGroupMemberMessage = "This member does not exist."
            }
        }
    }

    Column(
        modifier = Modifier
            .pointerInput(Unit) { detectTapGestures(onTap = { focusManager.clearFocus() }) }
            .padding(mediumPadding)
            .fillMaxWidth(),
    ) {
        Text(
            text = "Create a new group",
            style = MaterialTheme.typography.headlineLarge,
        )
        Spacer(modifier = Modifier.padding(smallPadding))
        groupName = TextFieldCard(labelValue = "Group name", input = groupName)
        Spacer(modifier = Modifier.padding(smallPadding))
        addMemberByPhoneNumber =
            TextFieldCard(labelValue = "Add member by phone number", input = addMemberByPhoneNumber)
        if (errorAddGroupMemberMessage.isNotEmpty()) {
            Text(
                text = errorAddGroupMemberMessage,
                color = MaterialTheme.colorScheme.error,
            )
        }
        Spacer(modifier = Modifier.padding(smallPadding))
        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                errorAddGroupMemberMessage = ""
                if (validatePhoneNumber(addMemberByPhoneNumber)) {
                    addItem(addMemberByPhoneNumber)
                } else {
                    errorAddGroupMemberMessage = "Invalid phone number."
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
                MemberCard(
                    memberName = returnNameFromId(item),
                    modifier = Modifier.padding(vertical = smallPadding)
                ) {
                    Text(text = "+45 $item")
                }
            }
        })
        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                if (validateGroup(groupName, memberList).isEmpty()) {
                    createGroup(
                        groupName,
                        memberList.toMutableList(),
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
) {
    val newMemberList = memberList
    newMemberList.add(userViewModel.activeUser.value.phoneNumber)
    val newGroup = Group()
    newGroup.name = groupName
    newGroup.members = newMemberList
    newGroup.createdBy = userViewModel.activeUser.value.documentID
    groupViewModel.postGroup(newGroup)
}

private fun validatePhoneNumber(phoneNumber: String): Boolean {
    return phoneNumber.isNotEmpty() && validatePhoneNumberIsNumbers(phoneNumber)
}

private fun validatePhoneNumberIsNumbers(phoneNumber: String): Boolean {
    return phoneNumber.matches(Regex("[0-9]+"))
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