package com.example.notweshare.screens

import android.content.ContentValues.TAG
import android.text.Editable
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.exampleapplication.viewmodels.UserViewModel
import com.example.notweshare.R
import com.example.notweshare.backend.FirestoreQueries
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.google.firebase.firestore.FirebaseFirestore
import com.example.notweshare.components.TextFieldCard

@Composable
fun ProfileScreen(userViewModel: UserViewModel) {
    val systemUiController = rememberSystemUiController()
    systemUiController.setStatusBarColor(MaterialTheme.colorScheme.primary)

    val mediumPadding = dimensionResource(R.dimen.padding_medium)
    val largePadding = dimensionResource(R.dimen.padding_large)

    Column(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(largePadding)
        ) {
            ProfileHeader(userViewModel = userViewModel)
            Spacer(modifier = Modifier.height(16.dp))
            ProfileDetails(userViewModel = userViewModel)
            Spacer(modifier = Modifier.height(32.dp))
            Button(
                onClick = {
                    Log.d(TAG, userViewModel.activeUser.value.phoneNumber)
                    Log.d(TAG, userViewModel.activeUser.value.email)

                    // Update userDocument in Firestore
                    val userRef = FirebaseFirestore.getInstance().collection("users").document(userViewModel.activeUser.value.phoneNumber)

                    userRef
                        .update("phoneNumber", userViewModel.activeUser.value.phoneNumber, "email", userViewModel.activeUser.value.email)
                        .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully updated!") }
                        .addOnFailureListener { e -> Log.w(TAG, "Error updating document", e) }

//                    FirestoreQueries.UserQueries.updateUser(userViewModel.activeUser.value)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            ) {
                Text(text = "Save")
            }
        }
    }
}

@Composable
fun ProfileHeader(userViewModel: UserViewModel) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Image(
            painter = painterResource(id = R.drawable.ic_launcher_foreground),
            contentDescription = null,
            modifier = Modifier
                .size(80.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primary)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = userViewModel.activeUser.value.name,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun ProfileDetails(userViewModel: UserViewModel) {
    Column {
        userViewModel.activeUser.value.phoneNumber = ProfileItem(
            icon = Icons.Default.Phone,
            title = "Phone",
            value = userViewModel.activeUser.value.phoneNumber,
            isEditable = false,
            onValueChange = { userViewModel.activeUser.value.phoneNumber = it }
        )

        userViewModel.activeUser.value.email = ProfileItem(
            icon = Icons.Default.Email,
            title = "Email",
            value = userViewModel.activeUser.value.email,
            isEditable = true,
            onValueChange = { userViewModel.activeUser.value.email = it }
        )
    }
}

@Composable
fun ProfileItem(
    icon: ImageVector,
    title: String,
    value: String,
    isEditable: Boolean,
    onValueChange: (String) -> Unit
): String {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp

    var userInfo by remember { mutableStateOf(value) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(24.dp)
        )

        Spacer(modifier = Modifier.width(16.dp))

        Text(
            text = title,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.width(16.dp))

        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.CenterEnd
        ) {
            if (isEditable) {
                userInfo = TextFieldCard(labelValue = title, input = value)
            } else {
                Text(
                    text = value,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
    return userInfo
}