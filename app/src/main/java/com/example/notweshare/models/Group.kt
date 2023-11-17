package com.example.notweshare.models

import java.io.Serializable
import java.util.*
import kotlin.math.exp

/**
 * @param createdBy This must be a member id
 */
data class Group(
    var name: String? = "",
    var expired: Boolean? = false,
    var members: MutableList<String> = mutableListOf(),
    var expenses: MutableList<Expense> = mutableListOf(),

    var createdBy: String = "",
    var createdAt: Date? = Date(),

    var documentID: String = "g"
) : Serializable {

    var memberDebts = mutableMapOf<String, Float>()
    var hasComputedMembers = false

    fun getTotalExpense(): Float {
        var out = 0f
        expenses.forEach{
            out += it.expenseAmount
        }

        return out
    }

    fun getMemberDebt(id: String): Float{
//        if (hasComputedMembers){
//            return memberDebts[id] ?: 0f
//        }

        computeMemberDebts()

        return memberDebts[id] ?: 0f
    }

    fun getMemberDebts(): MutableMap<String, Float>{
        computeMemberDebts()
        return memberDebts
    }

    private fun computeMemberDebts() {
        for (expense in expenses) {
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

        hasComputedMembers = true
    }
}

data class Expense(
    var name: String = "",
    var expenseAmount: Float = 0f,
    var members: MutableMap<String, ExpenseMember> = mutableMapOf(),
    var owner: String = "",

    var createdAt: Date? = Date(),
) : Serializable


// A member of the expense
data class ExpenseMember(
    var memberId: String = "",
    var payed: Boolean = false,
) : Serializable