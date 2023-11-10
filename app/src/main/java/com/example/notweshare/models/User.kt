package com.example.notweshare.models

import android.graphics.Bitmap
import java.io.Serializable
import java.util.*

data class User(
    var name: String? = "",
    var email: String? = "",
    var phoneNumber: String? = "",
    var newsletter: Boolean? = false,

    var createdAt: Date? = Date(),
    var createdAtString: String? = "",

    var documentID: String = "g"
): Serializable
