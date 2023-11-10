package com.example.exampleapplication.viewmodels

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.notweshare.backend.FirestoreQueries
import com.example.notweshare.models.User
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.launch

class UserViewModel(): ViewModel() {
    var listener: ListenerRegistration? = null
    //mutatable state such that Compose can observe it.
    val users = mutableStateListOf<User>()
    var isLoading = mutableStateOf(false)

    init {
        findUsers()
    }

    fun findUsers () {
        isLoading.value = true
        users.clear()
        viewModelScope.launch {
            fetchUsers(FirestoreQueries.UserQueries.allUsers()) { foundUsers ->
                users.clear()
                users.addAll(foundUsers)
                isLoading.value = false
            }
        }
    }

    private fun fetchUsers(queryCondition: Query, callback: (MutableList<User>) -> Unit) {
        var userArray: MutableList<User> = mutableListOf<User>()

        listener = queryCondition.addSnapshotListener(object : EventListener<QuerySnapshot> {
            override fun onEvent(
                value: QuerySnapshot?,
                error: FirebaseFirestoreException?
            ) {
                userArray = mutableListOf()

                if (error != null) {
                    Log.e("Firestore error", error.message.toString())
                    callback(mutableListOf())
                }

                value?.let { actualValue ->
                    for (document in actualValue.documents) {
                        Log.d("Success", document.toString())

                        document.toObject(User::class.java)?.let { foundObject ->
                            foundObject.documentID = document.id
                            userArray.add(foundObject)
                        }
                    }
                }

                callback(userArray)
            }
        })
    }
}