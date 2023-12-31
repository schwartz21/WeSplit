package com.example.notweshare.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.exampleapplication.viewmodels.GroupViewModel.Companion.groupViewModel
import com.example.exampleapplication.viewmodels.UserViewModel.Companion.userViewModel
import com.example.notweshare.R
import com.example.notweshare.models.Group
import com.example.notweshare.models.getMemberDebt
import com.example.notweshare.models.getTotalExpense
import com.example.notweshare.models.getTotalUnpaid
import java.text.DecimalFormat
import kotlin.math.abs

@Composable
fun GroupCard(
    group: Group,
    groupIndex: Int = 0,
    onNavigateToGroupDetails: () -> Unit = {},
) {
    val userContribution = getMemberDebt(group, userViewModel.activeUser.value.documentID)
    val absContribution = abs(userContribution)
    val userOwes = userContribution > 0

    GradientCard(
        onClickFunction = {
            groupViewModel.setTheSelectedGroup(groupIndex) // Set selected group to be the group that was clicked
            userViewModel.findUsersWithDocumentIDs(group.members) // Update list of users to be the users from the group
            onNavigateToGroupDetails()
        },
        text = group.name
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

@Composable
fun DoubleStack(
    topItem: String,
    bottomItem: String,
    owed: Boolean = false,
    positiveIsGreen: Boolean = false
) {
    val smallPadding = dimensionResource(R.dimen.padding_small)
    val mediumPadding = dimensionResource(R.dimen.padding_medium)

    val paymentColor = when {
        owed -> MaterialTheme.colorScheme.error
        positiveIsGreen && !owed -> MaterialTheme.colorScheme.surfaceVariant
        else -> MaterialTheme.colorScheme.onSurface
    }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Row(
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
            text = bottomItem,
            style = MaterialTheme.typography.labelMedium
        )
    }
}