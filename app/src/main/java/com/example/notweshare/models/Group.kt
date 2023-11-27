package com.example.notweshare.models

import java.io.Serializable
import java.util.*
import kotlin.random.Random

/**
 * @param createdBy This must be a member id
 */
data class Group(
    var name: String = "",
    var expired: Boolean = false,
    var members: MutableList<String> = mutableListOf(),
    var expenses: MutableList<Expense> = mutableListOf(),

    var createdBy: String = "",
    var createdAt: Date? = Date(),

    var documentID: String = "",
) : Serializable

fun getTotalExpense(group: Group): Float {
    var out = 0f
    group.expenses.forEach {
        out += it.expenseAmount
    }

    return out
}

fun getTotalUnpaid(group: Group): Float {
    val memberDebts = computeMemberDebts(group)

    var out = 0f
    val debts = memberDebts.values

    for (debt in debts) {
        if (debt <= 0) {
            continue
        }

        out += debt
    }

    return out
}

/**
 * @param id this should be a User.documentId
 */
fun getMemberDebt(group: Group, id: String): Float {
    val memberDebts = computeMemberDebts(group)

    println(memberDebts)

    return memberDebts[id] ?: 0f
}

fun getAllMemberDebts(group: Group): MutableMap<String, Float> {
    return computeMemberDebts(group)
}

private fun computeMemberDebts(group: Group): MutableMap<String, Float> {
    val memberDebts = mutableMapOf<String, Float>()
    for (expense in group.expenses) {
        val owner = expense.owner
        val expenseMembers = expense.members.values
        val equalShare = expense.expenseAmount / expenseMembers.size
        for (member in expenseMembers) {
            if (member.payed) {
                continue
            }

            memberDebts[owner] = (memberDebts[owner] ?: 0f) - equalShare
            memberDebts[member.memberId] = (memberDebts[member.memberId] ?: 0f) + equalShare
        }
    }

    return memberDebts
}


fun getDefaultGroup(): Group {
    return Group(
        name = "Test Group created by button",
        expired = false,
        members = mutableListOf("test", "test2"),
        expenses = mutableListOf(
            Expense(
                "testExp",
                Random.nextFloat() * 2000,
                mutableMapOf(
                    "test" to ExpenseMember("test"),
                    "test2" to ExpenseMember("test2", true)
                )
            )
        ),
        createdBy = "test",
        createdAt = Date(),
    )

}

data class Expense(
    var name: String = "",
    var expenseAmount: Float = 0f,
    var members: MutableMap<String, ExpenseMember> = mutableMapOf(),
    var owner: String = "",

    var createdAt: Date? = Date(),
) : Serializable


/**
 * A member of the expense
 *
 * @param memberId This refers to the User.documentId
 */
data class ExpenseMember(
    var memberId: String = "",
    var payed: Boolean = false,
) : Serializable