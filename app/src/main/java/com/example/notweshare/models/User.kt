package com.example.notweshare.models

import android.graphics.Bitmap
import java.io.Serializable
import java.util.*

data class User(
    var name: String? = "",
    var phoneNumber: String? = "",

    var createdAt: Date? = Date(),
    var createdAtString: String? = "",

    var documentID: String = "g"
): Serializable
