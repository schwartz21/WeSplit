package com.example.notweshare.screens

import android.Manifest
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
@OptIn(ExperimentalPermissionsApi::class)
fun PermissionDialog(){
    val permissionState = rememberPermissionState(permission = Manifest.permission.POST_NOTIFICATIONS)
    Log.d("NotificationService", "PermissionDialog 1")
    if(!permissionState.status.isGranted){
        Log.d("NotificationService", "PermissionDialog 2")
        Button(
            onClick = { permissionState.launchPermissionRequest() }) {
            Text(text = "Grant notifications on next screen")
        }
    }
}