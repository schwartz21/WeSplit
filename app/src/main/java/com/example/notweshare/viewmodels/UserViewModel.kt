package com.example.exampleapplication.viewmodels

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.notweshare.backend.FirestoreQueries
import com.example.notweshare.models.User
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.Query
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class UserViewModel(): ViewModel() {
    val users = mutableStateListOf<User>()
    var isLoading = mutableStateOf(false)

    val activeUser = mutableStateOf(User())

    fun setTheActiveUser(user: User) {
        activeUser.value = user
    }

    // Find users within a list of user document IDs
    fun findUsersWithDocumentIDs (userDocumentIDs: MutableList<String>, callback: (MutableList<User>) -> Unit) {
        isLoading.value = true
        viewModelScope.launch {
            fetchUsers(FirestoreQueries.UserQueries.usersWithDocumentIDs(userDocumentIDs)) { foundUsers ->
                users.clear()
                users.addAll(foundUsers)
                isLoading.value = false
                println(" users are ${users.toList()}")
            }
        }
    }

    //Find users with document id
    fun findUserWithDocumentID (userDocumentID: String, callback: (User) -> Unit) {
        isLoading.value = true
        viewModelScope.launch {
            fetchUsers(FirestoreQueries.UserQueries.userWithDocumentID(userDocumentID)) { foundUsers ->
                users.clear()
                users.addAll(foundUsers)
                isLoading.value = false
                if (users.isEmpty()) { users.add(User()) }
                callback(users[0])
            }
        }
    }

    fun postUser(user: User) {
        viewModelScope.launch {
            FirestoreQueries.UserQueries.postUser(user)
        }
    }

    private suspend fun fetchUsers(queryCondition: Query, callback: (MutableList<User>) -> Unit) {
        val userArray: MutableList<User> = mutableListOf<User>()

        val task = queryCondition.get()
        task.addOnCompleteListener {
            if (it.isSuccessful) {
                val documents = it.result
                if (documents != null) {
                    for (doc in documents) {
                        val user = doc.toObject(User::class.java)
                        user.documentID = doc.id
                        userArray.add(user)
                    }
                    callback(userArray)
                }
            }
        }
        task.await()
    }
}

private fun <TResult> Task<TResult>.addOnCompleteListener(
    userViewModel: UserViewModel,
    any: Any
): Any {
    return this
}
