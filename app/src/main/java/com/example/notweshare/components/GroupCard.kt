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
import androidx.compose.foundation.layout.wrapContentHeight
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
import androidx.compose.ui.text.style.TextAlign
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
import java.text.DecimalFormat

@Composable
fun GroupCard(
    group: Group,
    userViewModel: UserViewModel = koinViewModel()
) {
    val smallPadding = dimensionResource(R.dimen.padding_small)
    val mediumPadding = dimensionResource(R.dimen.padding_medium)

    Surface(
        modifier = Modifier.padding(mediumPadding),
        color = MaterialTheme.colorScheme.surfaceVariant,
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
                        110.sp.toDp()
                    })
                    .background(
                        Brush.verticalGradient(
                            colorStops = arrayOf(
                                0f to MaterialTheme.colorScheme.primary,
                                1f to MaterialTheme.colorScheme.tertiary
                            )
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = group.name ?: "unknown group name",
                    modifier = Modifier.padding(smallPadding),
                    color = MaterialTheme.colorScheme.onTertiary,
                    style = MaterialTheme.typography.displaySmall,
                    textAlign = TextAlign.Center,
                )
            }
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(mediumPadding)
            ) {
                DoubleStack(
                    topItem = DecimalFormat("#.##").format(getTotalExpense(group)),
                    bottomItem = "Total Expenses"
                )
                DoubleStack(
                    topItem = DecimalFormat("#.##").format(getTotalUnpaid(group)),
                    bottomItem = "Total Unpaid",
                    owed = getTotalUnpaid(group) > 0
                )
                DoubleStack(
                    topItem = DecimalFormat("#.##").format(
                        getMemberDebt(
                            group,
                            userViewModel.activeUser.documentID
                        )
                    ),
                    bottomItem = "You will receive" // TODO: change this text depending on - or positive
// TODO: Show green text when positive and red text when negative
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
        Spacer(modifier = Modifier.padding(smallPadding / 2))
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