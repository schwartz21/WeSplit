package com.example.exampleapplication.viewmodels

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.notweshare.backend.FirestoreQueries
import com.example.notweshare.models.Group
import com.example.notweshare.models.User
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.launch

class GroupViewModel(): ViewModel() {
    var listener: ListenerRegistration? = null
    //mutatable state such that Compose can observe it.
    val groups = mutableStateListOf<Group>()
    var isLoading = mutableStateOf(false)

    init {
        findGroups()
    }

    fun findGroups () {
        isLoading.value = true
        groups.clear()
        viewModelScope.launch {
            fetchGroups(FirestoreQueries.GroupQueries.allGroups()) { foundGroups ->
                groups.clear()
                groups.addAll(foundGroups)
                isLoading.value = false
            }
        }
    }

    fun findGroupsWithMember (memberDocumentID: String) {
        isLoading.value = true
        groups.clear()
        viewModelScope.launch {
            fetchGroups(FirestoreQueries.GroupQueries.groupsWithMember(memberDocumentID)) { foundGroups ->
                groups.clear()
                groups.addAll(foundGroups)
                isLoading.value = false
            }
        }
    }

    private fun fetchGroups(queryCondition: Query, callback: (MutableList<Group>) -> Unit) {
        var groupArray: MutableList<Group> = mutableListOf<Group>()

        listener = queryCondition.addSnapshotListener(object : EventListener<QuerySnapshot> {
            override fun onEvent(
                value: QuerySnapshot?,
                error: FirebaseFirestoreException?
            ) {
                groupArray = mutableListOf()

                if (error != null) {
                    Log.e("Firestore error", error.message.toString())
                    callback(mutableListOf())
                }

                value?.let { actualValue ->
                    for (document in actualValue.documents) {
                        Log.d("Success", document.toString())

                        document.toObject(Group::class.java)?.let { foundObject ->
                            foundObject.documentID = document.id
                            groupArray.add(foundObject)
                        }
                    }
                }

                callback(groupArray)
            }
        })
    }

    fun postGroup(group: Group) {
        viewModelScope.launch {
            FirestoreQueries.GroupQueries.postGroup(group)
        }
    }
}