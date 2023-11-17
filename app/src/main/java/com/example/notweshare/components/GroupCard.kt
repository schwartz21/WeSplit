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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.compose.AppTheme
import com.example.compose.md_success
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
import kotlin.math.abs

@Composable
fun GroupCard(
    group: Group,
    userViewModel: UserViewModel = koinViewModel()
) {
    val smallPadding = dimensionResource(R.dimen.padding_small)
    val mediumPadding = dimensionResource(R.dimen.padding_medium)

    val userContribution = getMemberDebt(group, userViewModel.activeUser.documentID)
    val absContribution = abs(userContribution)
    val userOwes = userContribution > 0


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
                    }).background(
                        Brush.verticalGradient(
                            colorStops = arrayOf(
                                0f to MaterialTheme.colorScheme.tertiaryContainer,
                                1f to MaterialTheme.colorScheme.tertiaryContainer
                            )
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = group.name ?: "unknown group name",
                    modifier = Modifier.padding(smallPadding),
                    color = MaterialTheme.colorScheme.onTertiaryContainer,
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
                    owed = getTotalUnpaid(group) > 0,
                    positiveIsGreen = true
                )
                DoubleStack(
                    topItem = DecimalFormat("#.##").format(absContribution),
                    bottomItem = if (userOwes) "You owe" else "You will receive",
                    owed = userOwes,
                    positiveIsGreen = true
// TODO: Show green text when positive and red text when negative
                )
            }
        }
    }
}

@Composable
fun DoubleStack(topItem: String, bottomItem: String, owed: Boolean = false, positiveIsGreen: Boolean = false) {
    val smallPadding = dimensionResource(R.dimen.padding_small)
    val mediumPadding = dimensionResource(R.dimen.padding_medium)

    val paymentColor = when {
        owed -> MaterialTheme.colorScheme.error
        positiveIsGreen && !owed -> md_success
        else -> MaterialTheme.colorScheme.onSurface
    }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Row(
//            modifier = Modifier.padding(),
            verticalAlignment = Alignment.Bottom
        ) {
            Text(
                text = topItem,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.ExtraBold,
                color = paymentColor
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