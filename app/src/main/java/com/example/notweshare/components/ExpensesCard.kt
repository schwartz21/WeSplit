package com.example.notweshare.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.notweshare.models.Expense

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
            text = expense.name,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.End,
        )
    }
}