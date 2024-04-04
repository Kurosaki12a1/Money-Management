package com.kuro.money.presenter.home.feature

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Alarm
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Notes
import androidx.compose.material.icons.filled.Share
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.kuro.money.R
import com.kuro.money.domain.model.LetterColor
import com.kuro.money.extension.noRippleClickable
import com.kuro.money.presenter.home.HomeViewModel
import com.kuro.money.presenter.utils.string
import com.kuro.money.presenter.utils.toPainterResource
import com.kuro.money.ui.theme.Gray

@Composable
fun TransactionDetailsScreen(
    navController: NavController,
    homeViewModel: HomeViewModel,
) {
    BackHandler { navController.popBackStack() }

    val transactionEntity = homeViewModel.selectedTransaction.collectAsState().value ?: return
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Gray)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(10.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Close",
                    modifier = Modifier.noRippleClickable {
                        navController.popBackStack()
                    })
                Spacer(modifier = Modifier.weight(1f))
                Icon(imageVector = Icons.Default.Share, contentDescription = "Share")
                Icon(imageVector = Icons.Default.Edit, contentDescription = "Edit")
                Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete")
            }
            if (transactionEntity.image != null) {
                Image(
                    painter = rememberAsyncImagePainter(model = transactionEntity.image),
                    contentDescription = "Photo",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    contentScale = ContentScale.Inside
                )
            }
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(20.dp),
                contentPadding = PaddingValues(10.dp)
            ) {
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(20.dp)
                    ) {
                        Image(
                            painter = transactionEntity.category.icon.toPainterResource(),
                            contentDescription = "Icon",
                            modifier = Modifier.weight(0.2f)
                        )
                        Text(
                            text = transactionEntity.category.name,
                            style = MaterialTheme.typography.body1,
                            modifier = Modifier.weight(0.8f)
                        )
                    }
                }
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Spacer(modifier = Modifier.weight(0.2f))
                        Text(
                            text = "${transactionEntity.amount.string()} ${transactionEntity.currency.symbol}",
                            style = MaterialTheme.typography.body1,
                            color = if (transactionEntity.category.type == "expense") Color.Red else Color.Cyan,
                            modifier = Modifier
                                .offset(x = 20.dp)
                                .weight(0.8f)
                        )
                    }
                }
                if (transactionEntity.note != null) {
                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(20.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Notes,
                                contentDescription = "Notes",
                                modifier = Modifier.weight(0.2f)
                            )
                            Text(
                                text = transactionEntity.note,
                                color = Color.Black.copy(0.5f),
                                style = MaterialTheme.typography.body2,
                                modifier = Modifier.weight(0.8f)
                            )
                        }
                    }
                }
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(20.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.CalendarMonth,
                            contentDescription = "Calendar",
                            modifier = Modifier.weight(0.2f)
                        )
                        Text(
                            text = transactionEntity.displayDate.string(),
                            style = MaterialTheme.typography.body2,
                            modifier = Modifier.weight(0.8f)
                        )
                    }
                }
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(20.dp)
                    ) {
                        Image(
                            painter = transactionEntity.wallet.icon.toPainterResource(),
                            contentDescription = "Wallet",
                            modifier = Modifier.weight(0.2f)
                        )
                        Text(
                            text = transactionEntity.wallet.name,
                            style = MaterialTheme.typography.body2,
                            modifier = Modifier.weight(0.8f)
                        )
                    }
                }
                if (transactionEntity.people != null) {
                    item {
                        val listOfPeople =
                            transactionEntity.people.split(",").filter { it.isNotEmpty() }
                                .map { it.trim() }
                        Column(verticalArrangement = Arrangement.spacedBy(20.dp)) {
                            Row(modifier = Modifier.fillMaxWidth()) {
                                Spacer(modifier = Modifier.weight(0.2f))
                                Text(
                                    text = stringResource(id = R.string.with),
                                    color = Color.Black,
                                    style = MaterialTheme.typography.body2,
                                    modifier = Modifier
                                        .offset(x = 20.dp)
                                        .weight(0.8f)
                                )
                            }
                            listOfPeople.forEach {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(20.dp)
                                ) {
                                    Box(modifier = Modifier.weight(0.2f)) {
                                        Box(
                                            modifier = Modifier
                                                .background(
                                                    LetterColor.getColor(it[0]),
                                                    CircleShape
                                                )
                                                .size(30.dp)
                                                .align(Alignment.Center)
                                        ) {
                                            Text(
                                                text = it[0].toString().uppercase(),
                                                color = Color.White,
                                                style = MaterialTheme.typography.body2,
                                                textAlign = TextAlign.Center,
                                                modifier = Modifier.align(Alignment.Center)
                                            )
                                        }
                                    }
                                    Text(
                                        text = it,
                                        color = Color.Black.copy(0.5f),
                                        style = MaterialTheme.typography.body2,
                                        modifier = Modifier.weight(0.8f)
                                    )
                                }
                            }
                        }

                    }
                }
                if (transactionEntity.event != null) {
                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(20.dp)
                        ) {
                            Image(
                                modifier = Modifier.weight(0.2f),
                                painter = transactionEntity.event.icon.toPainterResource(),
                                contentDescription = "Event"
                            )
                            Column(
                                modifier = Modifier.weight(0.8f),
                                verticalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Text(
                                    text = stringResource(id = R.string.events),
                                    color = Color.Black.copy(0.5f),
                                    style = MaterialTheme.typography.body2,
                                )
                                Text(
                                    text = transactionEntity.event.name,
                                    color = Color.Black.copy(0.5f),
                                    style = MaterialTheme.typography.body2,
                                )
                            }
                        }
                    }
                }
                if (transactionEntity.remindDate != null) {
                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(20.dp)
                        ) {
                            Icon(
                                modifier = Modifier.weight(0.2f),
                                imageVector = Icons.Default.Alarm,
                                contentDescription = "Alarm"
                            )
                            Text(
                                text = transactionEntity.remindDate.string(),
                                color = Color.Black.copy(0.5f),
                                style = MaterialTheme.typography.body2,
                                modifier = Modifier.weight(0.8f)
                            )
                        }
                    }
                }
            }
        }
    }
}