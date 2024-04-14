package com.kuro.money.presenter.home.feature

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EditCalendar
import androidx.compose.material.icons.filled.Notes
import androidx.compose.material.icons.filled.QuestionMark
import androidx.compose.material.icons.filled.Wallet
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.kuro.money.R
import com.kuro.money.data.utils.FileUtils
import com.kuro.money.data.utils.Resource
import com.kuro.money.extension.noRippleClickable
import com.kuro.money.navigation.routes.NavigationRoute
import com.kuro.money.presenter.add_transaction.MoreDetailsTransaction
import com.kuro.money.presenter.add_transaction.ToolbarAddTransaction
import com.kuro.money.presenter.add_transaction.showDatePicker
import com.kuro.money.presenter.utils.DecimalFormatter
import com.kuro.money.presenter.utils.DecimalInputVisualTransformation
import com.kuro.money.presenter.utils.popBackStackWithLifeCycle
import com.kuro.money.presenter.utils.toPainterResource
import com.kuro.money.ui.theme.Gray
import com.kuro.money.ui.theme.Green
import kotlinx.coroutines.flow.collectLatest
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun EditTransactionsDetailsScreen(
    navController: NavController,
    transactionId: Long,
    editTransactionDetailViewModel: EditTransactionDetailViewModel
) {
    BackHandler { navController.popBackStackWithLifeCycle() }

    if (navController.currentDestination?.route?.contains(NavigationRoute.Home.TransactionDetails.Edit.route) == true) {
        editTransactionDetailViewModel.getTransaction(transactionId)
    }

    val selectedCategory = editTransactionDetailViewModel.selectedCategory.collectAsState().value
    val selectedCurrency = editTransactionDetailViewModel.currencySelected.collectAsState().value
    val amount = editTransactionDetailViewModel.amount.collectAsState().value
    val wallet = editTransactionDetailViewModel.wallet.collectAsState().value
    val peopleSelected = editTransactionDetailViewModel.nameOfPeople.collectAsState().value
    val eventSelected = editTransactionDetailViewModel.eventSelected.collectAsState().value
    val imageSelectedFromGallery = editTransactionDetailViewModel.uriSelected.collectAsState().value
    val imageFromCamera = editTransactionDetailViewModel.bitmapFlow.collectAsState().value

    val dateTransaction = editTransactionDetailViewModel.dateTransaction.collectAsState().value
    val dateRemind = editTransactionDetailViewModel.dateRemind.collectAsState().value

    val context = LocalContext.current
    val shouldSubmitTransaction = remember { mutableStateOf(false) }

    LaunchedEffect(amount, selectedCategory, dateTransaction, wallet, selectedCurrency) {
        shouldSubmitTransaction.value = !(amount == null
                || wallet == null
                || selectedCurrency == null
                || selectedCategory == null)
    }

    LaunchedEffect(Unit) {
        editTransactionDetailViewModel.updateTransaction.collectLatest {
            if (it is Resource.Success) {
                navController.popBackStackWithLifeCycle()
            }
        }
    }

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(Gray)
    ) {
        val maxHeight = this.maxHeight
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            ToolbarAddTransaction(navController)
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(maxHeight * 0.8f)
                    .background(Gray),
                verticalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                item {
                    BodyAddTransaction(
                        editTransactionDetailViewModel,
                        onCurrencyClick = {
                            navController.navigate(NavigationRoute.Home.TransactionDetails.Edit.SelectCurrency.route)
                        },
                        onSelectCategoryClick = {
                            navController.navigate(NavigationRoute.Home.TransactionDetails.Edit.SelectCategory.route)
                        },
                        onNoteClick = {
                            navController.navigate(NavigationRoute.Home.TransactionDetails.Edit.Note.route)
                        },
                        onWalletClick = {
                            navController.navigate(NavigationRoute.Home.TransactionDetails.Edit.SelectWallet.route)
                        })
                }
                item {
                    MoreDetailsTransaction(
                        peopleSelected,
                        eventSelected,
                        dateRemind,
                        imageSelectedFromGallery,
                        imageFromCamera,
                        onSelectPeopleClick = { navController.navigate(NavigationRoute.Home.TransactionDetails.Edit.With.route) },
                        onSelectEventClick = {
                            navController.navigate(NavigationRoute.Home.TransactionDetails.Edit.SelectEvent.route)
                        },
                        onAlarmClick = {
                            showDatePicker(context, true) {
                                editTransactionDetailViewModel.setDateRemind(it)
                            }
                        },
                        onImagePicked = {
                            editTransactionDetailViewModel.setUriSelected(it)
                        },
                        onCameraPicked = {
                            editTransactionDetailViewModel.setBitmap(it)
                        }
                    )
                }
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .align(Alignment.CenterHorizontally)
                    .padding(10.dp)
                    .size(40.dp)
                    .background(
                        if (!shouldSubmitTransaction.value) Color.White.copy(0.5f) else Green,
                        RoundedCornerShape(16.dp)
                    )
                    .noRippleClickable {
                        if (imageFromCamera != null) {
                            editTransactionDetailViewModel.setUriSelected(
                                FileUtils.getUriForFile(
                                    context, FileUtils.saveBitmapToFile(
                                        context, imageFromCamera
                                    )
                                )
                            )
                        }
                        if (shouldSubmitTransaction.value) {
                            editTransactionDetailViewModel.setDefaultCurrency()
                            editTransactionDetailViewModel.submitData()
                        }
                    },
            ) {
                Text(
                    modifier = Modifier.align(Alignment.Center),
                    text = stringResource(id = R.string.save),
                    textAlign = TextAlign.Center,
                    color = Color.Black.copy(alpha = 0.7f)
                )
            }
        }
    }
}


@Composable
private fun BodyAddTransaction(
    editTransactionDetailViewModel: EditTransactionDetailViewModel,
    onCurrencyClick: () -> Unit,
    onSelectCategoryClick: () -> Unit,
    onNoteClick: () -> Unit,
    onWalletClick: () -> Unit
) {
    val context = LocalContext.current
    val selectedCategory = editTransactionDetailViewModel.selectedCategory.collectAsState().value
    val selectedCurrency = editTransactionDetailViewModel.currencySelected.collectAsState().value
    val amount = editTransactionDetailViewModel.amount.collectAsState().value
    val note = editTransactionDetailViewModel.note.collectAsState().value
    val wallet = editTransactionDetailViewModel.wallet.collectAsState().value
    val dateTransaction = editTransactionDetailViewModel.dateTransaction.collectAsState().value

    val textTime = if (dateTransaction == LocalDate.now()) stringResource(id = R.string.today)
    else DateTimeFormatter.ofPattern("dd/MM/yyyy").format(dateTransaction)
    val decimalFormatter = DecimalFormatter()

    Surface(
        modifier = Modifier.fillMaxWidth(), color = Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 10.dp),
            verticalArrangement = Arrangement.spacedBy(30.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                if (selectedCurrency != null) {
                    Image(
                        painter = selectedCurrency.icon.toPainterResource(),
                        contentDescription = selectedCurrency.name,
                        modifier = Modifier.noRippleClickable { onCurrencyClick() }
                    )
                }
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = amount ?: "",
                    onValueChange = {
                        editTransactionDetailViewModel.setAmount(decimalFormatter.cleanup(it))
                    },
                    singleLine = true,
                    label = { Text(text = stringResource(id = R.string.amount)) },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Decimal,
                        imeAction = ImeAction.Done
                    ),
                    visualTransformation = DecimalInputVisualTransformation(decimalFormatter)
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onSelectCategoryClick() },
                horizontalArrangement = Arrangement.spacedBy(20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (selectedCategory != null) {
                    Image(
                        painter = selectedCategory.icon.toPainterResource(),
                        contentDescription = selectedCategory.name
                    )
                    Text(
                        text = selectedCategory.name,
                        style = MaterialTheme.typography.h6,
                        color = Color.Black.copy(alpha = 0.7f)
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .background(Gray, CircleShape)
                            .size(36.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Filled.QuestionMark,
                            contentDescription = "Question",
                            tint = Color.Black.copy(alpha = 0.3f)
                        )
                    }
                    Text(
                        text = stringResource(id = R.string.select_category),
                        style = MaterialTheme.typography.h6,
                        color = Color.Black.copy(alpha = 0.7f)
                    )
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onNoteClick() },
                horizontalArrangement = Arrangement.spacedBy(20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Filled.Notes,
                    contentDescription = "Notes",
                    tint = Color.Black,
                    modifier = Modifier.size(30.dp)
                )
                Text(
                    text = note.ifEmpty { stringResource(id = R.string.write_note) },
                    style = MaterialTheme.typography.body1,
                    color = Color.Black.copy(alpha = 0.7f)
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        showDatePicker(context) {
                            editTransactionDetailViewModel.setDateTransaction(it)
                        }
                    },
                horizontalArrangement = Arrangement.spacedBy(20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Filled.EditCalendar,
                    contentDescription = "Calendar",
                    tint = Color.Black,
                    modifier = Modifier.size(30.dp)
                )
                Text(
                    text = textTime,
                    style = MaterialTheme.typography.body1,
                    color = Color.Black.copy(alpha = 0.7f)
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onWalletClick() },
                horizontalArrangement = Arrangement.spacedBy(20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (wallet == null) {
                    Icon(
                        imageVector = Icons.Filled.Wallet,
                        contentDescription = "Wallet",
                        tint = Color.Black,
                        modifier = Modifier.size(30.dp)
                    )
                    Text(
                        text = stringResource(id = R.string.cash),
                        style = MaterialTheme.typography.body1,
                        color = Color.Black.copy(alpha = 0.7f)
                    )
                } else {
                    Image(
                        painter = wallet.icon.toPainterResource(),
                        contentDescription = "Wallet",
                        modifier = Modifier.size(30.dp)
                    )
                    Text(
                        text = wallet.name,
                        style = MaterialTheme.typography.body1,
                        color = Color.Black.copy(alpha = 0.7f)
                    )
                }
            }
        }
    }
}