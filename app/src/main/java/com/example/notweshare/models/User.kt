package com.example.notweshare.models

import java.io.Serializable
import java.util.*

/**
 * The User model, which is used to represent a user in the app.
 * If a user is not found, the default user is returned.
 * The default user contains empty strings for all fields except for the documentID field.
 *
 * @param name The name of the user
 * @param phoneNumber The phone number of the user
 * @param email The email of the user
 * @param password The password of the user
 * @param createdAt The date the user was created
 * @param documentID The document ID, which is the same as the phonenumber
 */
data class User(
    var name: String = "",
    var phoneNumber: String = "",
    var email: String = "",
    var password: String = "",

    var createdAt: Date? = Date(),

    var documentID: String = ""
): Serializable
