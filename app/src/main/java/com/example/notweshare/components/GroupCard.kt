package com.example.notweshare.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.exampleapplication.viewmodels.UserViewModel
import com.example.notweshare.R
import com.example.notweshare.models.Expense
import com.example.notweshare.models.ExpenseMember
import com.example.notweshare.models.Group
import com.example.notweshare.models.User
import org.koin.androidx.compose.koinViewModel
import java.util.Date
import kotlin.random.Random

@Composable
fun GroupCard(
    group: Group,
    userViewModel: UserViewModel = koinViewModel()
) {
    val smallPadding = dimensionResource(R.dimen.padding_small)
    val mediumPadding = dimensionResource(R.dimen.padding_medium)

    println("##############################################3")
    println(userViewModel.users.isEmpty())
    if (userViewModel.users.isEmpty()){
        userViewModel.users.add(User())
    }
    println(userViewModel)

    Surface(
        modifier = Modifier.padding(mediumPadding),
        color = Color(0xFFB3E5FC),
        shape = RoundedCornerShape(mediumPadding)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp),
                color = Color(0xFFA5D6A7)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(smallPadding),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = group.name ?: "unknown group name"
                    )
                }
            }
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(smallPadding)
            ) {
                DoubleStack(
                    topItem = group.getTotalExpense().toString(),
                    bottomItem = "Total Expenses"
                )
                DoubleStack(
                    topItem = group.getTotalUnpaid().toString(),
                    bottomItem = "Total Unpaid"
                )
                DoubleStack(
                    topItem = group.getMemberDebt(userViewModel.users[0].name).toString(),
                    bottomItem = "You will receive"
                )
            }
        }
    }
}

@Composable
fun DoubleStack(topItem: String, bottomItem: String) {
    val smallPadding = dimensionResource(R.dimen.padding_small)
    val mediumPadding = dimensionResource(R.dimen.padding_medium)

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            modifier = Modifier.padding(smallPadding),
            text = topItem
        )
        Text(
            modifier = Modifier.padding(smallPadding),
            text = bottomItem
        )
    }
}


//@Preview(showBackground = true)
//@Composable
//fun GroupCardPreview() {
//    val userViewModel = UserViewModel()
//    userViewModel.users.add(User())
//
//    GroupCard(
//        group = Group(
//            name = "Test Group created by button",
//            expired = false,
//            members = mutableListOf("test", "test2"),
//            expenses = mutableListOf(
//                Expense(
//                    "testExp",
//                    Random.nextFloat() * 2000,
//                    mutableMapOf("test" to ExpenseMember("test"), "test2" to ExpenseMember("test2", true))
//                )
//            ),
//            createdBy = "test",
//            createdAt = Date(),
//        ),
//        userViewModel = userViewModel
//    )
//}