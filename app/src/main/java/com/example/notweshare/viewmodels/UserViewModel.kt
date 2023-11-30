package com.example.exampleapplication.viewmodels

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.notweshare.backend.FirestoreQueries
import com.example.notweshare.models.User
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.launch

class UserViewModel() : ViewModel() {

    companion object {
        val userViewModel = UserViewModel()
    }

    var listener: ListenerRegistration? = null
    val users = mutableStateListOf<User>()
    var isLoading = mutableStateOf(false)

    val activeUser = mutableStateOf(User())

    fun setTheActiveUser(user: User) {
        activeUser.value = user
    }

    // Find users within a list of user document IDs
    fun findUsersWithDocumentIDs(userDocumentIDs: MutableList<String>) {
        isLoading.value = true
        viewModelScope.launch {
            fetchUsers(FirestoreQueries.UserQueries.usersWithDocumentIDs(userDocumentIDs)) { foundUsers ->
                users.clear()
                users.addAll(foundUsers)
                for (user in foundUsers){
                    if (user.documentID == activeUser.value.documentID){
                        setTheActiveUser(user)
                    }
                }
                isLoading.value = false
            }
        }
    }

    /**
     * Find a user with a document ID
     * @param userDocumentID: The document ID of the user to find
     * @param callback: A callback function to execute after the user is found. If no user is found with that document id, returns the default user
     */
    fun findUserWithDocumentID(userDocumentID: String, callback: (User) -> Unit) {
        isLoading.value = true
        viewModelScope.launch {
            fetchUsers(FirestoreQueries.UserQueries.userWithDocumentID(userDocumentID)) { foundUsers ->
                var userDidNotExist = true

                for (i in 0..users.size-1) {
                    if (!users.isEmpty() && users[i].documentID == foundUsers[0].documentID) {
                        users[i] = foundUsers[0]
                        userDidNotExist = false
                    }
                }

                for (user in foundUsers){
                    if (user.documentID == activeUser.value.documentID){
                        setTheActiveUser(user)
                    }
                }

                if (userDidNotExist) {
                    users.addAll(foundUsers)
                }
                isLoading.value = false
                // To ensure that the callback is executed, even if the user is not found
                if (users.isEmpty()) {
                    users.add(User())
                    callback(users[0])
                    return@fetchUsers
                }
                callback(foundUsers[0])
            }
        }
    }

    fun postUser(user: User) {
        viewModelScope.launch {
            FirestoreQueries.UserQueries.postUser(user)
        }
    }

    fun editUser(user: User) {
        viewModelScope.launch {
            FirestoreQueries.UserQueries.updateUser(user)
        }
    }

    private fun fetchUsers(queryCondition: Query, callback: (MutableList<User>) -> Unit) {
        var userArray: MutableList<User>

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

