package com.example.notweshare.backend

import com.example.notweshare.models.Expense
import com.example.notweshare.models.Group
import com.example.notweshare.models.User
import com.google.android.gms.tasks.Task
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
            // Find users within the list of user document IDs
            @JvmStatic fun usersWithDocumentIDs(userDocumentIDs: MutableList<String>): Query {
                return FirebaseFirestore.getInstance().collection(FirestoreQueries.userCollectionPath).whereIn(FieldPath.documentId(), userDocumentIDs)
            }
            // Post a new user
            @JvmStatic fun postUser(user: User): Task<Void> {
                val fbCollection = FirebaseFirestore.getInstance().collection(FirestoreQueries.userCollectionPath)
                return fbCollection.document(user.phoneNumber).set(user)
            }

            @JvmStatic fun updateUser(user: User): Task<Void> {
                val fbCollection = FirebaseFirestore.getInstance().collection(FirestoreQueries.userCollectionPath)
                return fbCollection.document(user.phoneNumber).update("phoneNumber", user.phoneNumber, "email", user.email, "userToken", user.userToken, "name", user.name)
            }
        }
    }
    class GroupQueries {
        companion object {
            @JvmStatic fun allGroups(): Query {
                return FirebaseFirestore.getInstance().collection("groups")
            }
            @JvmStatic fun groupWithDocumentID(groupDocumentID: String): Query {
                return FirebaseFirestore.getInstance().collection("groups").whereEqualTo(FieldPath.documentId(), groupDocumentID)
            }
            @JvmStatic fun groupsWithMember(memberDocumentID: String): Query {
                return FirebaseFirestore.getInstance().collection("groups").whereArrayContains("members", memberDocumentID)
            }
            // Post a new group
            @JvmStatic fun postGroup(group: Group): Task<DocumentReference> {
                val fbCollection = FirebaseFirestore.getInstance().collection("groups")
                return fbCollection.add(group)
            }
            // Add expense to a group
            @JvmStatic fun updateExpensesOnAGroup(groupDocumentID: String, expenses: MutableList<Expense>): Task<Void> {
                return FirebaseFirestore.getInstance().collection("groups").document(groupDocumentID).update("expenses", expenses)
            }
            // Update a group with a new group
            @JvmStatic fun updateGroup(group: Group): Task<Void> {
                val fbCollection = FirebaseFirestore.getInstance().collection("groups")
                return fbCollection.document(group.documentID).update("name", group.name, "members", group.members, "expenses", group.expenses)
            }
        }
    }
}