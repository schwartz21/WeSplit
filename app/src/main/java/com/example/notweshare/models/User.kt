package com.example.notweshare.models

import java.io.Serializable
import java.util.*

data class User(
    var name: String = "",
    var phoneNumber: String = "",
    var email: String = "",
    var password: String = "",

    var createdAt: Date? = Date(),

    var documentID: String = "g"
): Serializable
