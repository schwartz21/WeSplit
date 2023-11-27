package com.example.notweshare.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.notweshare.R

@Composable
fun GradientCard(
    text: String,
    onClickFunction: () -> Unit = {},
    content: @Composable () -> Unit
) {
    val smallPadding = dimensionResource(R.dimen.padding_small)
    val mediumPadding = dimensionResource(R.dimen.padding_medium)

    Surface(
        modifier = Modifier.padding(mediumPadding),
        color = MaterialTheme.colorScheme.surfaceVariant,
        shape = RoundedCornerShape(mediumPadding),
        shadowElevation = 4.dp,
        onClick = onClickFunction
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
                                0f to MaterialTheme.colorScheme.primary,
                                1f to MaterialTheme.colorScheme.tertiary
                            )
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
//                Text(
//                    text = text,
//                    modifier = Modifier.padding(smallPadding),
//                    color = MaterialTheme.colorScheme.onPrimary,
//                    style = MaterialTheme.typography.titleLarge,
//                    textAlign = TextAlign.Center,
//                )
                HeaderDisplay(string = text)
            }
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(mediumPadding)
            ) {
                content()
            }
        }
    }

}

@Composable
fun HeaderDisplay(string: String){
    val displayText = remember{
        mutableStateOf(string)
    }
    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth(),
        colors = OutlinedTextFieldDefaults.colors(
//            focusedContainerColor = MaterialTheme.colorScheme.background,
//            unfocusedContainerColor = MaterialTheme.colorScheme.background,
//            disabledContainerColor = MaterialTheme.colorScheme.onBackground,
//            cursorColor = MaterialTheme.colorScheme.onBackground,
//            focusedBorderColor = MaterialTheme.colorScheme.primary,
//            focusedLabelColor = MaterialTheme.colorScheme.onBackground,
        ),
        textStyle = MaterialTheme.typography.titleLarge.copy(textAlign = TextAlign.Center, color = MaterialTheme.colorScheme.onPrimary),
        shape = textFieldShape,
        keyboardOptions = KeyboardOptions.Default,
        value = displayText.value,
        onValueChange = {
            displayText.value = it
        }
    )
}
