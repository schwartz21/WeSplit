package com.example.notweshare.models

import androidx.annotation.DrawableRes

data class TabItem(
    val title: String,
    val route: String,
    @DrawableRes val icon: Int
)
