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
import com.example.notweshare.R
import com.example.notweshare.models.Expense
import com.example.notweshare.models.Group
import java.util.Date
import kotlin.random.Random

@Composable
fun GroupCard(group: Group) {
    val smallPadding = dimensionResource(R.dimen.padding_small)
    val mediumPadding = dimensionResource(R.dimen.padding_medium)
    Surface(
        modifier = Modifier.padding(mediumPadding),
        color = Color(0xFFB3E5FC),
        shape = RoundedCornerShape(mediumPadding)
    ) {
        Column (
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
                Text(
                    modifier = Modifier.padding(smallPadding),
                    text = group.createdBy.toString() ?: "Unknown amount"
                )
                Text(
                    modifier = Modifier.padding(smallPadding),
                    text = group.getTotalExpense().toString() ?: "Unknown expiration"
                )
                Text(
                    modifier = Modifier.padding(smallPadding),
                    text = group.expired.toString() ?: "Unknown expiration"
                )
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun GroupCardPreview() {
    GroupCard(
        group = Group(
            name = "Test Group created by button",
            expired = false,
            members = mutableListOf("test", "test2"),
            expenses = mutableListOf(Expense("testExp", Random.nextFloat()*2000)),
            createdBy = "test",
            createdAt = Date(),
        )
    )
}