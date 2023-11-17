package com.example.notweshare.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.exampleapplication.viewmodels.UserViewModel
import com.example.notweshare.models.Expense
import com.example.notweshare.models.Group
import org.koin.androidx.compose.koinViewModel
import java.util.Date

@Composable
fun GroupDetailsScreen(
    navigation: NavController,
    group: Group = Group(
        name = "Test Group created by button",
        expired = false,
        members = mutableListOf("test", "test2"),
        expenses = mutableListOf(),
        createdBy = "test",
        createdAt = Date(),
    ),
    userViewModel: UserViewModel = koinViewModel(),
) {
    LazyColumn(modifier = Modifier.padding(10.dp)) {
        item {
            //GroupCard From Thor
        }
        items(items = group.members){
            member -> GroupDetailsMemberCard(member)
        }
        item {
            Text(
                text = "Expenses:",
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.headlineMedium,
            )
            Spacer(modifier = Modifier.padding(5.dp))
        }
        items(items = group.expenses){
            expense -> ExpensesCard(expense)
        }
    }
}




@Composable
fun GroupDetailsMemberCard(member: String) {
    Row(
        modifier = Modifier
            .padding(bottom = 10.dp)
            .height(IntrinsicSize.Min)
            .fillMaxSize()
            .background(
                color = Color.Gray,
                shape = RoundedCornerShape(10.dp)
            )
            .padding(10.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            modifier = Modifier.fillMaxWidth(.6f),
            text = member,
            fontSize = 24.sp,
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End,
            modifier = Modifier.fillMaxWidth(1f),
        ) {
            Text(
                text = "200 kr.",
                color = Color.Red,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(end = 5.dp)
            )
            Button(
                onClick = { /*TODO*/ }
            ) {
                Text(text = "N")
            }
        }
    }
}

@Composable
fun ExpensesCard(expense: Expense) {
    Row(
        modifier = Modifier
            .padding(bottom = 10.dp)
            .height(IntrinsicSize.Min)
            .fillMaxSize()
            .background(
                color = Color.Gray,
                shape = RoundedCornerShape(10.dp)
            )
            .padding(10.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column() {
            Text(
                text = "Amount: ${expense.expenseAmount}",
                fontSize = 24.sp,
            )
            Text(
                text = expense.createdAt.toString(),
                fontSize = 15.sp
            )
        }
        Text(
            text = expense.name.toString(),
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.End,
        )
    }
}
