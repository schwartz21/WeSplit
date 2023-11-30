package com.example.notweshare.components

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.compose.AppTheme
import com.example.notweshare.R


@Composable
fun Point(text: String = "Point x"){
    Box(
        modifier = Modifier.height(55.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.titleSmall,
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
fun Slide(titleText: String = "Components/Reusability", content: @Composable () -> Unit) {
    val smallPadding = dimensionResource(R.dimen.padding_small)
    val mediumPadding = dimensionResource(R.dimen.padding_medium)

    GradientCard(
        text = titleText
    ) {
        content()
    }
}

@Composable
fun Backend() {
    Slide(titleText = "Backend"){
        Point(text = "Firebase")
        Point(text = "Dynamic updates")
    }
}

@Composable
fun NotificationsSlide() {
    Slide(titleText = "Notifications"){
        Point(text = "")
//        Point(text = "NotificationCompat builder")
//        Point(text = "Intents")
    }
}

@Composable
fun LofiSlide() {
    Slide(titleText = ""){
        Point(text = "")
    }
}

@Composable
fun TitleSlide() {
    Slide(titleText = "Android Native"){
        Point(text = "Group 11")
        Point(text = "-")
        Point(text = "WeSplit")
//        Point(text = "NotificationCompat builder")
//        Point(text = "Intents")
    }
}

@Preview(showBackground = true,backgroundColor = 0xFF001429, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun SlidePreview() {
    AppTheme {
//        TitleSlide()
//        LofiSlide()
//        NotificationsSlide()
        Backend()
    }
}


