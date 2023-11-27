package com.example.notweshare.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.notweshare.R
import com.example.notweshare.models.Expense
import java.text.DecimalFormat
import java.text.SimpleDateFormat

@Composable
fun ExpensesCard(expense: Expense) {

    val largePadding = dimensionResource(R.dimen.padding_large)
    val mediumPadding = dimensionResource(R.dimen.padding_medium)
    val smallPadding = dimensionResource(R.dimen.padding_small)

    Surface(
        modifier = Modifier.padding(horizontal = mediumPadding),
        color = MaterialTheme.colorScheme.surfaceVariant,
        shape = RoundedCornerShape(mediumPadding),
        shadowElevation = 4.dp,
    ) {
        Row(
            modifier = Modifier
                .height(IntrinsicSize.Min)
                .fillMaxSize()
                .padding(horizontal = mediumPadding, vertical = smallPadding),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column() {
                Text(
                    text = "Amount: ${DecimalFormat("#.##").format(expense.expenseAmount)}",
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.bodyLarge,
                )
                Text(
                    text = SimpleDateFormat.getDateTimeInstance().format(expense.createdAt),
                    style = MaterialTheme.typography.bodySmall,
                )
            }
            Spacer(modifier = Modifier.width(smallPadding))
            Text(
                text = expense.name,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.End,
                style = MaterialTheme.typography.bodyMedium,
            )
        }
    }
}