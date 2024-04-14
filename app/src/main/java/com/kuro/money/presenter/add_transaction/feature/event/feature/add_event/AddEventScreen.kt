package com.kuro.money.presenter.add_transaction.feature.event.feature.add_event

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.QuestionMark
import androidx.compose.material.icons.filled.Wallet
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.kuro.money.R
import com.kuro.money.data.utils.Resource
import com.kuro.money.extension.noRippleClickable
import com.kuro.money.presenter.add_transaction.showDatePicker
import com.kuro.money.presenter.utils.SlideUpContent
import com.kuro.money.presenter.utils.popBackStackWithLifeCycle
import com.kuro.money.presenter.utils.string
import com.kuro.money.presenter.utils.toPainterResource
import com.kuro.money.ui.theme.Gray
import kotlinx.coroutines.flow.collectLatest

@Composable
fun AddEventScreen(
    navController: NavController,
    addEventScreenViewModel: AddEventScreenViewModel,
    onSelectIcon: () -> Unit,
    onSelectCurrency: () -> Unit,
    onSelectWallet: () -> Unit
) {
    BackHandler { navController.popBackStackWithLifeCycle() }

    val iconSelected = addEventScreenViewModel.iconSelected.collectAsState().value
    println("IconSelected from AddEvent: $iconSelected")
    val walletSelected = addEventScreenViewModel.wallet.collectAsState().value
    val currencySelected = addEventScreenViewModel.currencySelected.collectAsState().value

    val name = addEventScreenViewModel.name.collectAsState().value
    val endingDate = addEventScreenViewModel.endingDate.collectAsState().value
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        addEventScreenViewModel.insertEvent.collectLatest {
            if (it is Resource.Success) {
                navController.popBackStackWithLifeCycle()
            }
        }
    }

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Close",
                    modifier = Modifier.clickable {
                        navController.popBackStackWithLifeCycle()
                    })
                Text(
                    text = stringResource(id = R.string.add_event),
                    color = Color.Black,
                    style = MaterialTheme.typography.h6,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = stringResource(id = R.string.save),
                    color = Color.Black,
                    style = MaterialTheme.typography.body1,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.noRippleClickable {
                        addEventScreenViewModel.insertEvent()
                    }
                )
            }
            Divider(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(Gray)
            )
            SlideUpContent(isVisible = true) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(30.dp)
                ) {
                    /* Select Icon and Name */
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(20.dp)
                    ) {
                        if (iconSelected == null) {
                            Box(
                                modifier = Modifier
                                    .noRippleClickable {
                                        onSelectIcon()
                                    }
                                    .background(Gray, CircleShape)
                                    .size(30.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.QuestionMark,
                                    contentDescription = "Question",
                                    tint = Color.Black.copy(alpha = 0.3f)
                                )
                            }
                        } else {
                            Image(
                                modifier = Modifier.noRippleClickable {
                                    onSelectIcon()

                                },
                                painter = iconSelected.toPainterResource(),
                                contentDescription = iconSelected
                            )
                        }
                        Box(contentAlignment = Alignment.CenterStart) {
                            if (name.isEmpty()) {
                                Text(
                                    text = stringResource(id = R.string.app_name),
                                    color = Color.Black.copy(alpha = 0.5f),
                                    style = MaterialTheme.typography.body1,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .align(Alignment.CenterStart)
                                )
                            }
                            BasicTextField(
                                value = name,
                                onValueChange = { value -> addEventScreenViewModel.setName(value) },
                                textStyle = TextStyle(
                                    color = Color.Black.copy(alpha = 0.5f),
                                    fontSize = 18.sp,
                                ),
                                singleLine = true,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .align(Alignment.CenterStart)
                            )
                        }
                    }

                    /* Select Ending Date */
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .noRippleClickable {
                                showDatePicker(context) {
                                    addEventScreenViewModel.setEndingDate(it)
                                }
                            },
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(20.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.CalendarMonth,
                            contentDescription = "Ending Date"
                        )
                        Text(
                            text = endingDate.string(),
                            color = Color.Black.copy(alpha = 0.5f),
                            style = MaterialTheme.typography.body1
                        )
                    }

                    /* Select Currency */
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .noRippleClickable {
                                onSelectCurrency()
                            },
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(20.dp)
                    ) {
                        if (currencySelected == null) {
                            Box(
                                modifier = Modifier
                                    .background(Color.Black, CircleShape)
                                    .size(30.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.AttachMoney,
                                    contentDescription = "Money",
                                    tint = Color.White
                                )
                            }
                            Text(
                                text = stringResource(id = R.string.currency),
                                color = Color.Black.copy(alpha = 0.5f),
                                style = MaterialTheme.typography.body1
                            )
                        } else {
                            Image(
                                painter = currencySelected.icon.toPainterResource(),
                                contentDescription = currencySelected.name
                            )
                            Text(
                                text = currencySelected.name,
                                color = Color.Black.copy(alpha = 0.5f),
                                style = MaterialTheme.typography.body1
                            )
                        }
                    }

                    /* Select Wallet */

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .noRippleClickable { onSelectWallet() },
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(20.dp)
                    ) {
                        if (walletSelected == null) {
                            Icon(
                                imageVector = Icons.Filled.Wallet,
                                contentDescription = "Wallet",
                                tint = Color.Black
                            )
                            Text(
                                text = stringResource(id = R.string.select_wallet),
                                color = Color.Black.copy(alpha = 0.5f),
                                style = MaterialTheme.typography.body1,
                            )
                        } else {
                            Image(
                                painter = walletSelected.icon.toPainterResource(),
                                contentDescription = "Wallet",
                            )
                            Text(
                                text = walletSelected.name,
                                color = Color.Black.copy(alpha = 0.5f),
                                style = MaterialTheme.typography.body1,
                            )
                        }
                    }
                }
            }
        }
    }
}