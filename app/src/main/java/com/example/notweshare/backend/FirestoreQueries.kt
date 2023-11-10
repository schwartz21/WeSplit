package com.example.notweshare.backend

import com.google.firebase.firestore.*

class FirestoreQueries {
    companion object {
        @JvmStatic val userCollectionPath = "users"
    }

    class UserQueries {
        companion object {
            @JvmStatic fun allUsers(): Query {
                return FirebaseFirestore.getInstance().collection(FirestoreQueries.userCollectionPath)
            }
            @JvmStatic fun userWithDocumentID(userDocumentID: String): Query {
                return FirebaseFirestore.getInstance().collection(FirestoreQueries.userCollectionPath).whereEqualTo(FieldPath.documentId(), userDocumentID)
            }

            @JvmStatic fun userWithPhoneNumber(userPhoneNumber: String): Query {
                return FirebaseFirestore.getInstance().collection(FirestoreQueries.userCollectionPath).whereEqualTo("phoneNumber", userPhoneNumber)
            }
        }
    }
}