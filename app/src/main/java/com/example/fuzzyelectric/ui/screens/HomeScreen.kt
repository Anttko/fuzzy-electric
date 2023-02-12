package com.example.fuzzyelectric.ui.screens

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.BitmapFactory
import android.os.Build
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.himanshoe.charty.bar.BarChart
import com.himanshoe.charty.bar.model.BarData
import com.example.fuzzyelectric.R
import com.example.fuzzyelectric.model.Prices
import com.example.fuzzyelectric.ui.theme.FuzzyElectricTheme
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.expandVertically
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.fuzzyelectric.model.Compare
import com.example.fuzzyelectric.notification.Notification
import com.himanshoe.charty.bar.config.BarConfig
import com.himanshoe.charty.common.axis.AxisConfig
import com.himanshoe.charty.horizontalbar.HorizontalBarChart
import com.himanshoe.charty.horizontalbar.model.HorizontalBarData
import java.time.format.DateTimeFormatter
import kotlin.math.roundToInt
import kotlin.math.roundToLong


@Composable
fun HomeScreen(
    fuzzyElectricUiState: FuzzyElectricUiState,
    retryAction: () -> Unit,
    modifier: Modifier = Modifier,
    navigateButton: (Int) -> Unit
) {
    when (fuzzyElectricUiState) {
        is FuzzyElectricUiState.Loading -> LoadingScreen(modifier)
        is FuzzyElectricUiState.Success -> PricesView(
            fuzzyElectricUiState.prices,
            modifier,
            navigateButton
        )
        else -> ErrorScreen(retryAction, modifier)
    }
}

@Composable
fun LoadingScreen(modifier: Modifier = Modifier) {
    Box(
        contentAlignment = Alignment.Center, modifier = modifier.fillMaxSize()
    ) {
        Image(
            modifier = Modifier.size(200.dp),
            painter = painterResource(R.drawable.loading_img),
            contentDescription = stringResource(R.string.loading)
        )
    }
}

@Composable
fun ErrorScreen(retryAction: () -> Unit, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(stringResource(R.string.loading_failed))
        Button(onClick = retryAction) {
            Text(stringResource(R.string.retry))
        }
    }
}

@Composable
private fun PricesView(
    prices: List<Prices>,
    modifier: Modifier = Modifier,
    navigateButton: (Int) -> Unit
) {
    val context = LocalContext.current
    var avg = 0.0
    val dataToDisplayed = mutableListOf<HorizontalBarData>()

    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssz")
    val displayFormatter = DateTimeFormatter.ofPattern("HH:mm")

    prices.forEach { price ->
        val parsedDate = formatter.parse(price.DateTime)
        var item =
            HorizontalBarData(
                price.PriceWithTax.toFloat(),
                displayFormatter.format(parsedDate).toString()
            )
        avg += price.PriceWithTax
        dataToDisplayed.add(item)
    }
    val minPrice = prices.reduce(Compare::min)
    val maxPrice = prices.reduce(Compare::max)
    val avgPrice = String.format("%.4f", (avg / prices.size))

    val notify = Notification(
        context,
        "Prices Updated",
        "Low: ${minPrice.PriceWithTax}€,  Average: $avgPrice€, High: ${maxPrice.PriceWithTax}€"
    )
    notify.messageNotification()

    //val testData: List<BarData> =
    //    listOf(BarData("10:00", 2f), BarData("11:00", 3f), BarData("12:00", 1.5f))
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
    ) {
        item {
            Column(
                modifier = modifier
                    .padding(bottom = 0.dp)
                    .fillMaxSize()
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = modifier
                        .fillMaxSize()
                ) {

                    Text(
                        modifier = Modifier
                            .vertical()
                            .padding(start = 16.dp)
                            .rotate(90f),
                        text = "Hours",
                        fontSize = 16.sp
                    )
                    HorizontalBarChart(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(500.dp)
                            .padding(16.dp),
                        onBarClick = { },
                        color = Color.Black,
                        horizontalBarData = dataToDisplayed,
                    )
                }

            }
        }
        item {
            Text(
                text = "Lowest price: ${minPrice.PriceWithTax} €/kwH",
                fontSize = 16.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                textAlign = TextAlign.Center,
                color = MaterialTheme.colors.secondaryVariant,
                style = MaterialTheme.typography.subtitle2
            )
            Text(
                text = "Highest price: ${maxPrice.PriceWithTax} €/kwH",
                fontSize = 16.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                textAlign = TextAlign.Center,
                color = MaterialTheme.colors.secondaryVariant,
                style = MaterialTheme.typography.subtitle2
            )
            Text(
                text = "Average price: $avgPrice €/kwH",
                fontSize = 16.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                textAlign = TextAlign.Center,
                color = MaterialTheme.colors.secondaryVariant,
                style = MaterialTheme.typography.subtitle2
            )

            OutlinedButton(onClick = {
                val notify = Notification(
                    context,
                    "Prices Updated",
                    "Low: ${minPrice.PriceWithTax}€,  Average: $avgPrice€, High: ${maxPrice.PriceWithTax}€"
                )
                notify.messageNotification()
            }) {
                Text(text = "test notification")
            }
            Button(onClick = { navigateButton(1) }) {
                Text(text = "text")
            }

        }
    }
}

fun Modifier.vertical() = layout { measurable, constraints ->
    val placeable = measurable.measure(constraints)
    layout(placeable.height, placeable.width) {
        placeable.place(
            x = -(placeable.width / 2 - placeable.height / 2),
            y = -(placeable.height / 2 - placeable.width / 2)
        )
    }
}
