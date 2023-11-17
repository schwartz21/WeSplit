package com.example.notweshare.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.compose.AppTheme
import com.example.exampleapplication.viewmodels.UserViewModel
import com.example.notweshare.R
import com.example.notweshare.models.Group
import com.example.notweshare.models.User
import com.example.notweshare.models.getDefaultGroup
import com.example.notweshare.models.getMemberDebt
import com.example.notweshare.models.getTotalExpense
import com.example.notweshare.models.getTotalUnpaid
import org.koin.androidx.compose.koinViewModel

@Composable
fun GroupCard(
    group: Group,
    userViewModel: UserViewModel = koinViewModel()
) {
    val smallPadding = dimensionResource(R.dimen.padding_small)
    val mediumPadding = dimensionResource(R.dimen.padding_medium)

    Surface(
        modifier = Modifier.padding(mediumPadding),
        color = MaterialTheme.colorScheme.surface,
        shape = RoundedCornerShape(mediumPadding),
        shadowElevation = 8.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(with(LocalDensity.current) {
                        100.sp.toDp()
                    })
                    .background(
                        Brush.verticalGradient(
                            colorStops = arrayOf(
                                0f to MaterialTheme.colorScheme.tertiary,
                                1f to MaterialTheme.colorScheme.primary
                            )
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = group.name ?: "unknown group name",
                    modifier = Modifier.padding(smallPadding),
                    color = MaterialTheme.colorScheme.onPrimary,
                    style = MaterialTheme.typography.displaySmall
                )
            }
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(mediumPadding)
            ) {
                DoubleStack(
                    topItem = getTotalExpense(group).toString(),
                    bottomItem = "Total Expenses"
                )
                DoubleStack(
                    topItem = getTotalUnpaid(group).toString(),
                    bottomItem = "Total Unpaid",
                    owed = getTotalUnpaid(group) > 0
                )
                DoubleStack(
                    topItem = getMemberDebt(group, userViewModel.activeUser.documentID).toString(),
                    bottomItem = "You will receive"
                )
            }
        }
    }
}

@Composable
fun DoubleStack(topItem: String, bottomItem: String, owed: Boolean = false) {
    val smallPadding = dimensionResource(R.dimen.padding_small)
    val mediumPadding = dimensionResource(R.dimen.padding_medium)

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Row(
//            modifier = Modifier.padding(),
            verticalAlignment = Alignment.Bottom
        ) {
            Text(
                text = topItem,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                color = if (owed) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.padding(with(LocalDensity.current) {
                0.8.sp.toDp()
            }))
            Text(
                text = "kr.",
                style = MaterialTheme.typography.bodySmall,
            )
        }
        Spacer(modifier = Modifier.padding(smallPadding/2))
        Text(
//            modifier = Modifier.padding(smallPadding),
            text = bottomItem,
            style = MaterialTheme.typography.labelMedium
        )
    }
}


@Preview(showBackground = true)
@Composable
fun GroupCardPreview() {
    val userViewModel = UserViewModel()
    userViewModel.users.add(User())

    AppTheme {
        GroupCard(
            group = getDefaultGroup(),
            userViewModel = userViewModel
        )

    }
}