package com.example.notweshare.components

import androidx.compose.foundation.clickable
import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.exampleapplication.viewmodels.GroupViewModel.Companion.groupViewModel
import com.example.exampleapplication.viewmodels.UserViewModel.Companion.userViewModel
import com.example.notweshare.R
import com.example.notweshare.models.Expense
import com.example.notweshare.models.ExpenseMember
import com.example.notweshare.models.Group
import com.example.notweshare.models.getMemberDebt
import com.example.notweshare.notification.NotificationService
import java.text.DecimalFormat
import kotlin.math.abs


@Composable
fun MemberCard(memberName: String, modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    val largePadding = dimensionResource(R.dimen.padding_large)
    val mediumPadding = dimensionResource(R.dimen.padding_medium)
    val smallPadding = dimensionResource(R.dimen.padding_small)

    Surface(
        modifier = modifier,
        color = MaterialTheme.colorScheme.surface,
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
            Text(
                modifier = Modifier.fillMaxWidth(.6f),
                text = memberName,
                style = MaterialTheme.typography.bodyLarge,
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End,
                modifier = Modifier.fillMaxWidth(1f),
            ) {
                content()
            }
        }
    }
}

@Composable
fun GroupDetailsMemberCard(context: Context, group: Group, member: String, memberName: String) {

    val userViewModel = userViewModel
    val notification = NotificationService(context)

    val largePadding = dimensionResource(R.dimen.padding_large)
    val mediumPadding = dimensionResource(R.dimen.padding_medium)
    val smallPadding = dimensionResource(R.dimen.padding_small)

    val userContribution = getMemberDebt(group, member)
    val absContribution = abs(userContribution)
    val userOwes = userContribution > 0

    val paymentColor = when {
        userOwes -> MaterialTheme.colorScheme.error
        !userOwes -> MaterialTheme.colorScheme.surfaceVariant
        else -> MaterialTheme.colorScheme.onSurface
    }

    MemberCard(
        memberName = memberName,
        modifier = Modifier.padding(horizontal = mediumPadding)
    ) {
        if (userOwes && member == userViewModel.activeUser.value.documentID)
            Icon(
                painter = painterResource(id = R.drawable.payup),
                contentDescription = "Pay",
                tint = paymentColor,
                modifier = Modifier
                    .size(28.dp)
                    .padding(end = smallPadding / 2)
                    .clickable { payMember(member, group) }
            )
        Text(
            text = "${DecimalFormat("#.##").format(absContribution)} kr.",
            color = paymentColor,
            fontWeight = FontWeight.Bold,
        )
        if (userOwes)
            Icon(
                painter = painterResource(id = R.drawable.notification),
                contentDescription = "Pay",
                tint = paymentColor,
                modifier = Modifier
                    .size(36.dp)
                    .padding(start = smallPadding / 2)
                    .clickable {
                        notification.showNotification(
                            userViewModel.activeUser.value.name,
                            userContribution.toString(),
                            group.name,
                            memberName
                        )
                    }
            )
    }
}

// Go through all expenses in the group with the member
// If payed boolean is false, set to true
private fun payMember(member: String, group: Group) {
    val out = group.copy()
    group.expenses.forEach() { expense ->
        val expenseMembers = expense.members.values

        for (i in 0..expenseMembers.size - 1) {
            if (expenseMembers.elementAt(i).memberId == member && !expenseMembers.elementAt(i).payed) {
                expenseMembers.elementAt(i).payed = true
                expense.members.replace(i.toString(), expenseMembers.elementAt(i))
            }
        }
    }

    // Post new group
    groupViewModel.updateGroup(out)
}
