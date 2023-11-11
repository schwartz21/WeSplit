package com.example.notweshare.models

import java.io.Serializable
import java.util.*

data class Group(
    var name: String? = "",
    var expired: Boolean? = false,
    var members: MutableList<String> = mutableListOf(),
    var expenses: MutableList<Expense> = mutableListOf(),

    var createdBy: String? = "",
    var createdAt: Date? = Date(),

    var documentID: String = "g"
): Serializable

data class Expense(
    var name: String? = "",
    var expenseAmount: Int? = 0,
    var members: MutableMap<String, ExpenseMember> = mutableMapOf(),

    var createdAt: Date? = Date(),
): Serializable


// A member of the expense
data class ExpenseMember(
    var memberId: String? = "",
    var payed: Boolean? = false,
): Serializable