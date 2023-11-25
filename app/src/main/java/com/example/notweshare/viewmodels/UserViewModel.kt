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

    val activeUser = mutableStateOf(User())

    // Will be removed once login screen works
    init {
        findUsers()
    }

    fun setTheActiveUser(user: User) {
        activeUser.value = user
    }

    fun findUsers () {
        isLoading.value = true
        viewModelScope.launch {
            fetchUsers(FirestoreQueries.UserQueries.allUsers()) { foundUsers ->
                users.clear()
                users.addAll(foundUsers)
                isLoading.value = false
                setTheActiveUser(foundUsers[0]) // Will be removed once we have login
            }
        }
    }

    fun findUserWithDocumentId (documentId: String): User {
        var out = User()
        isLoading.value = true
        viewModelScope.launch {
            fetchUser(FirestoreQueries.UserQueries.userWithDocumentId(documentId)) { foundUser ->
                users.clear()
                users.add(foundUser)
                println(foundUser)
                isLoading.value = false
                out = foundUser
                println("fetch user out: $out")
            }
        }
        return out
    }

    private fun fetchUser(queryCondition: Query, callback: (User) -> Unit) {
        listener = queryCondition.addSnapshotListener(object : EventListener<QuerySnapshot> {
            override fun onEvent(
                value: QuerySnapshot?,
                error: FirebaseFirestoreException?
            ) {
                var user: User? = null

                if (error != null) {
                    Log.e("Firestore error", error.message.toString())
                    callback(User())
                }

                value?.let { actualValue ->
                    for (document in actualValue.documents) {
                        Log.d("Success", document.toString())

                        document.toObject(User::class.java)?.let { foundObject ->
                            foundObject.documentID = document.id
                            user = foundObject
                        }
                    }
                }

                callback(user ?: User())
            }
        })
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