package com.example.notweshare.backend

import android.util.Log
import com.example.notweshare.models.User
import com.google.firebase.firestore.*

object FetchUsers {

    init {

    }

    var listener: ListenerRegistration? = null

    fun findUsers(queryCondition: Query, callback: (MutableList<User>) -> Unit) {
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