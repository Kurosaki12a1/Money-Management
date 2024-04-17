package com.kuro.money.presenter.transactions.feature.advance_search

import android.app.DatePickerDialog
import android.content.Context
import androidx.activity.compose.BackHandler
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Divider
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavController
import com.kuro.money.R
import com.kuro.money.data.AppCache
import com.kuro.money.domain.model.AdvancedSearchAmount
import com.kuro.money.domain.model.AdvancedSearchCategory
import com.kuro.money.domain.model.AdvancedSearchTime
import com.kuro.money.extension.noRippleClickable
import com.kuro.money.navigation.routes.NavigationRoute
import com.kuro.money.presenter.utils.DecimalFormatter
import com.kuro.money.presenter.utils.DecimalInputVisualTransformation
import com.kuro.money.presenter.utils.popBackStackWithLifeCycle
import com.kuro.money.presenter.utils.string
import com.kuro.money.ui.theme.Gray
import java.time.LocalDate

@Composable
fun AdvancedSearchTransactionScreen(
    navController: NavController,
    advancedSearchViewModel: AdvanceSearchViewModel
) {
    BackHandler { navController.popBackStackWithLifeCycle() }
    val context = LocalContext.current
    val noteText = remember { mutableStateOf("") }
    val withText = remember { mutableStateOf("") }
    val amountText = remember { mutableStateOf<AdvancedSearchAmount>(AdvancedSearchAmount.All) }
    val timeText = remember { mutableStateOf<AdvancedSearchTime>(AdvancedSearchTime.All) }
    val walletSelected = advancedSearchViewModel.walletSelected.collectAsState().value
    val categoryText = remember { mutableStateOf(AdvancedSearchCategory.ALL_CATEGORIES) }

    val enableOptionsAmount =
        remember { mutableStateOf<AdvancedSearchAmount>(AdvancedSearchAmount.Disabled) }

    val enableOptionsTime =
        remember { mutableStateOf<AdvancedSearchTime>(AdvancedSearchTime.Disabled) }

    val enableOptionsCategory = remember { mutableStateOf(false) }
    val enableAmountDialog = remember { mutableStateOf(false) }
    val enableTimeDialog = remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Gray)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(horizontal = 20.dp, vertical = 20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Close",
                modifier = Modifier.noRippleClickable { navController.popBackStackWithLifeCycle() })
            Text(
                text = stringResource(id = R.string.search),
                style = MaterialTheme.typography.h6,
                color = Color.Black,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = stringResource(id = R.string.search),
                style = MaterialTheme.typography.body1,
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.noRippleClickable {
                    advancedSearchViewModel.querySearch(
                        amount = amountText.value,
                        category = categoryText.value,
                        with = withText.value,
                        note = noteText.value,
                        time = timeText.value,
                    )
                    //TODO need wait data load done first then navigate
                    navController.navigate(NavigationRoute.Transaction.AdvancedSearchTransaction.SearchResult.route)
                }
            )
        }
        Divider(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(Gray)
        )
        /**
         * Amount Option
         */
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(20.dp),
            horizontalArrangement = Arrangement.spacedBy(15.dp)
        ) {
            Text(
                text = stringResource(id = R.string.amount_tv),
                color = Color.Black.copy(0.5f),
                style = MaterialTheme.typography.body1,
                fontWeight = FontWeight.Bold
            )
            Column(
                verticalArrangement = Arrangement.spacedBy(5.dp),
                modifier = Modifier.noRippleClickable {
                    enableOptionsAmount.value = AdvancedSearchAmount.Enabled
                }
            ) {
                Text(
                    text = amountText.value.string(),
                    color = Color.Black,
                    style = MaterialTheme.typography.body1,
                    modifier = Modifier.offset(x = 10.dp)
                )
                Divider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(Gray)
                )
            }
            OptionsMenuAmount(
                isExpanded = enableOptionsAmount.value == AdvancedSearchAmount.Enabled,
                onDismissRequest = {
                    enableOptionsAmount.value = AdvancedSearchAmount.Disabled
                },
                options = listOf(
                    AdvancedSearchAmount.All,
                    AdvancedSearchAmount.Over(),
                    AdvancedSearchAmount.Under(),
                    AdvancedSearchAmount.Between(),
                    AdvancedSearchAmount.Exact()
                )
            ) { options ->
                enableOptionsAmount.value = options
                if (enableOptionsAmount.value !is AdvancedSearchAmount.All) {
                    enableAmountDialog.value = true
                } else {
                    amountText.value = AdvancedSearchAmount.All
                }
            }
        }

        /**
         * Wallet Options
         */
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(20.dp),
            horizontalArrangement = Arrangement.spacedBy(15.dp)
        ) {
            Text(
                text = stringResource(id = R.string.wallet_tv),
                color = Color.Black.copy(0.5f),
                style = MaterialTheme.typography.body1,
                fontWeight = FontWeight.Bold
            )
            Column(
                verticalArrangement = Arrangement.spacedBy(5.dp),
                modifier = Modifier.noRippleClickable {
                    navController.navigate(NavigationRoute.Transaction.AdvancedSearchTransaction.SelectWallet.route)
                }
            ) {
                Text(
                    text = walletSelected,
                    color = Color.Black,
                    style = MaterialTheme.typography.body1,
                    modifier = Modifier.offset(x = 10.dp)
                )
                Divider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(Gray)
                )
            }
        }

        /**
         * Time Options
         */
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(20.dp),
            horizontalArrangement = Arrangement.spacedBy(15.dp)
        ) {
            Text(
                text = stringResource(id = R.string.time_tv),
                color = Color.Black.copy(0.5f),
                style = MaterialTheme.typography.body1,
                fontWeight = FontWeight.Bold
            )
            Column(
                verticalArrangement = Arrangement.spacedBy(5.dp),
                modifier = Modifier.noRippleClickable {
                    enableOptionsTime.value = AdvancedSearchTime.Enabled
                }
            ) {
                Text(
                    text = timeText.value.string(),
                    color = Color.Black,
                    style = MaterialTheme.typography.body1,
                    modifier = Modifier.offset(x = 10.dp)
                )
                Divider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(Gray)
                )
            }
            OptionsMenuTime(
                isExpanded = enableOptionsTime.value == AdvancedSearchTime.Enabled,
                onDismissRequest = {
                    enableOptionsTime.value = AdvancedSearchTime.Disabled
                },
                options = listOf(
                    AdvancedSearchTime.All,
                    AdvancedSearchTime.After(),
                    AdvancedSearchTime.Before(),
                    AdvancedSearchTime.Between(),
                    AdvancedSearchTime.Exact()
                ),
                onSelect = {
                    enableOptionsTime.value = it
                    when (enableOptionsTime.value) {
                        is AdvancedSearchTime.All -> timeText.value = AdvancedSearchTime.All

                        is AdvancedSearchTime.Between -> {
                            enableTimeDialog.value = true
                        }

                        is AdvancedSearchTime.Exact -> {
                            showDatePicker(context) { date ->
                                timeText.value = AdvancedSearchTime.Exact(date)
                            }
                        }

                        is AdvancedSearchTime.Before -> {
                            showDatePicker(context) { date ->
                                timeText.value = AdvancedSearchTime.Before(date)
                            }
                        }

                        is AdvancedSearchTime.After -> {
                            showDatePicker(context) { date ->
                                timeText.value = AdvancedSearchTime.After(date)
                            }
                        }

                        else -> {
                            /** Who care **/
                            /** Who care **/
                        }
                    }
                }
            )
        }

        /**
         * Note Option
         */

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(20.dp),
            horizontalArrangement = Arrangement.spacedBy(15.dp)
        ) {
            Text(
                text = stringResource(id = R.string.note_tv),
                color = Color.Black.copy(0.5f),
                style = MaterialTheme.typography.body1,
                fontWeight = FontWeight.Bold
            )
            Column(
                verticalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                Box {
                    if (noteText.value.isBlank()) {
                        Text(
                            text = stringResource(id = R.string.note),
                            color = Color.Black.copy(0.5f),
                            style = MaterialTheme.typography.body1
                        )
                    }
                    BasicTextField(
                        value = noteText.value,
                        onValueChange = { noteText.value = it },
                        singleLine = true,
                        textStyle = TextStyle(
                            color = Color.Black,
                            fontSize = MaterialTheme.typography.body1.fontSize
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                Divider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(Gray)
                )
            }
        }

        /**
         * Category Options
         */

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(20.dp),
            horizontalArrangement = Arrangement.spacedBy(15.dp)
        ) {
            Text(
                text = stringResource(id = R.string.category_tv),
                color = Color.Black.copy(0.5f),
                style = MaterialTheme.typography.body1,
                fontWeight = FontWeight.Bold
            )
            Column(
                verticalArrangement = Arrangement.spacedBy(5.dp),
                modifier = Modifier.noRippleClickable {
                    enableOptionsCategory.value = true
                }
            ) {
                Text(
                    text = categoryText.value.value,
                    color = Color.Black,
                    style = MaterialTheme.typography.body1
                )
                Divider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(Gray)
                )
            }
            OptionsMenuCategory(
                isExpanded = enableOptionsCategory.value,
                onDismissRequest = {
                    enableOptionsCategory.value = false
                },
                options = AdvancedSearchCategory.entries,
                onSelect = {
                    categoryText.value = it
                })
        }

        /**
         * With
         */


        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(20.dp),
            horizontalArrangement = Arrangement.spacedBy(15.dp)
        ) {
            Text(
                text = stringResource(id = R.string.with_tv),
                color = Color.Black.copy(0.5f),
                style = MaterialTheme.typography.body1,
                fontWeight = FontWeight.Bold
            )
            Column(
                verticalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                Box {
                    if (withText.value.isBlank()) {
                        Text(
                            text = stringResource(id = R.string.with),
                            color = Color.Black.copy(0.5f),
                            style = MaterialTheme.typography.body1
                        )
                    }
                    BasicTextField(
                        value = withText.value,
                        onValueChange = { withText.value = it },
                        singleLine = true,
                        textStyle = TextStyle(
                            color = Color.Black,
                            fontSize = MaterialTheme.typography.body1.fontSize
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                Divider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(Gray)
                )
            }
        }
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(10.dp)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .noRippleClickable {
                    navController.navigate(NavigationRoute.Transaction.SearchTransaction.route)
                },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(id = R.string.simple),
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.h6,
                textAlign = TextAlign.Center,
                color = Color.Cyan,
            )
            Icon(
                imageVector = Icons.Default.ArrowDropUp,
                contentDescription = "Arrow",
                tint = Color.Cyan
            )
        }
    }
    AmountDialog(
        visible = enableAmountDialog.value,
        onDismissRequest = { enableAmountDialog.value = false },
        isBetweenOptions = enableOptionsAmount.value is AdvancedSearchAmount.Between,
        onDone = { value1, value2 ->
            enableAmountDialog.value = false
            when (enableOptionsAmount.value) {
                is AdvancedSearchAmount.Over -> {
                    amountText.value = AdvancedSearchAmount.Over(value1)
                }

                is AdvancedSearchAmount.Under -> {
                    amountText.value = AdvancedSearchAmount.Under(value1)
                }

                is AdvancedSearchAmount.Between -> {
                    amountText.value = AdvancedSearchAmount.Between(value1, value2)
                }

                is AdvancedSearchAmount.Exact -> {
                    amountText.value = AdvancedSearchAmount.Exact(value1)
                }

                else -> {}
            }
        }
    )
    TimeDialog(
        visible = enableTimeDialog.value,
        onDismissRequest = { enableTimeDialog.value = false },
        onDone = { date1, date2 ->
            when (enableOptionsTime.value) {
                is AdvancedSearchTime.Between -> {
                    timeText.value = AdvancedSearchTime.Between(date1, date2)
                }

                else -> {}
            }
        }
    )
}

@Composable
private fun OptionsMenuAmount(
    isExpanded: Boolean,
    onDismissRequest: () -> Unit,
    options: List<AdvancedSearchAmount>,
    onSelect: (AdvancedSearchAmount) -> Unit
) {
    DropdownMenu(expanded = isExpanded, onDismissRequest = onDismissRequest) {
        options.forEachIndexed { _, s ->
            DropdownMenuItem(
                modifier = Modifier.padding(10.dp),
                onClick = { onSelect(s) }) {
                Text(text = s.name(), style = MaterialTheme.typography.body1, color = Color.Black)
            }
        }
    }
}

@Composable
private fun OptionsMenuTime(
    isExpanded: Boolean,
    onDismissRequest: () -> Unit,
    options: List<AdvancedSearchTime>,
    onSelect: (AdvancedSearchTime) -> Unit
) {
    DropdownMenu(expanded = isExpanded, onDismissRequest = onDismissRequest) {
        options.forEachIndexed { _, s ->
            DropdownMenuItem(
                modifier = Modifier.padding(10.dp),
                onClick = {
                    onSelect(s)
                    onDismissRequest()
                }) {
                Text(text = s.name(), style = MaterialTheme.typography.body1, color = Color.Black)
            }
        }
    }
}

@Composable
private fun OptionsMenuCategory(
    isExpanded: Boolean,
    onDismissRequest: () -> Unit,
    options: List<AdvancedSearchCategory>,
    onSelect: (AdvancedSearchCategory) -> Unit
) {
    DropdownMenu(expanded = isExpanded, onDismissRequest = onDismissRequest) {
        options.forEachIndexed { _, s ->
            DropdownMenuItem(
                modifier = Modifier.padding(10.dp),
                onClick = {
                    onSelect(s)
                    onDismissRequest()
                }) {
                Text(text = s.value, style = MaterialTheme.typography.body1, color = Color.Black)
            }
        }
    }
}

@Composable
private fun AmountDialog(
    visible: Boolean,
    onDismissRequest: () -> Unit,
    isBetweenOptions: Boolean,
    onDone: (Double, Double) -> Unit
) {
    if (!visible) return
    val decimalFormatter = DecimalFormatter()
    val firstAmount = remember { mutableStateOf("0") }
    val secondAmount = remember { mutableStateOf("0") }
    val symbol = AppCache.defaultCurrencyEntity.collectAsState().value?.symbol
    Dialog(
        properties = DialogProperties(usePlatformDefaultWidth = false),
        onDismissRequest = { onDismissRequest() }) {
        Column(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .background(Color.White, RoundedCornerShape(16.dp))
                .padding(vertical = 10.dp, horizontal = 30.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Text(
                text = stringResource(id = R.string.enter_amount),
                color = Color.Black,
                style = MaterialTheme.typography.h6,
                fontWeight = FontWeight.Bold
            )
            Row(modifier = Modifier.fillMaxWidth()) {
                BasicTextField(
                    value = firstAmount.value,
                    onValueChange = {
                        firstAmount.value = decimalFormatter.cleanup(it)
                    },
                    visualTransformation = DecimalInputVisualTransformation(decimalFormatter),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Decimal,
                        imeAction = ImeAction.Done
                    ),
                    singleLine = true,
                    textStyle = TextStyle(
                        fontSize = MaterialTheme.typography.body1.fontSize,
                        textAlign = TextAlign.Start,
                        color = Color.Black
                    ),
                    cursorBrush = SolidColor(Color.Green),
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = symbol ?: "",
                    color = Color.Black.copy(0.5f)
                )
            }
            if (isBetweenOptions) {
                Row(modifier = Modifier.fillMaxWidth()) {
                    BasicTextField(
                        value = secondAmount.value,
                        onValueChange = {
                            secondAmount.value = decimalFormatter.cleanup(it)
                        },
                        visualTransformation = DecimalInputVisualTransformation(decimalFormatter),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Decimal,
                            imeAction = ImeAction.Done
                        ),
                        singleLine = true,
                        textStyle = TextStyle(
                            fontSize = MaterialTheme.typography.body1.fontSize,
                            textAlign = TextAlign.Start,
                            color = Color.Black
                        ),
                        cursorBrush = SolidColor(Color.Green),
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        text = symbol ?: "",
                        color = Color.Black.copy(0.5f)
                    )
                }
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = stringResource(id = android.R.string.cancel).uppercase(),
                    color = Color.Green,
                    style = MaterialTheme.typography.body1,
                    modifier = Modifier.noRippleClickable { onDismissRequest() }
                )
                Text(
                    text = stringResource(id = R.string.done).uppercase(),
                    color = Color.Green,
                    style = MaterialTheme.typography.body1,
                    modifier = Modifier.noRippleClickable {
                        // Avoid string start with 0000 so convert to double and convert string
                        onDone(
                            firstAmount.value.toDouble(),
                            secondAmount.value.toDouble()
                        )
                    }
                )
            }
        }
    }
}

@Composable
private fun TimeDialog(
    visible: Boolean,
    onDismissRequest: () -> Unit,
    onDone: (LocalDate, LocalDate) -> Unit
) {
    if (!visible) return
    val context = LocalContext.current
    val startDate = remember { mutableStateOf(LocalDate.now()) }
    val endDate = remember { mutableStateOf(LocalDate.now()) }
    Dialog(
        properties = DialogProperties(usePlatformDefaultWidth = false),
        onDismissRequest = { onDismissRequest() }) {
        Column(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .background(Color.White, RoundedCornerShape(16.dp))
                .padding(vertical = 10.dp, horizontal = 30.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Text(
                text = stringResource(id = R.string.select_time),
                color = Color.Black,
                style = MaterialTheme.typography.h6,
                fontWeight = FontWeight.Bold
            )
            /**
             * FROM DATE
             */
            Text(
                text = stringResource(id = R.string.from),
                style = MaterialTheme.typography.body1,
                color = Color.Black,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        showDatePicker(context) {
                            startDate.value = it
                        }
                    },
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                Text(
                    text = startDate.value.string(),
                    color = Color.Black,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.body1
                )
                Divider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(Gray)
                )
            }
            /**
             * TO DATE
             */
            Text(
                text = stringResource(id = R.string.to),
                style = MaterialTheme.typography.body1,
                color = Color.Black,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        showDatePicker(context) {
                            endDate.value = it
                        }
                    },
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                Text(
                    text = endDate.value.string(),
                    color = Color.Black,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.body1
                )
                Divider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Gray)
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = stringResource(id = android.R.string.cancel).uppercase(),
                    color = Color.Green,
                    style = MaterialTheme.typography.body1,
                    modifier = Modifier.noRippleClickable { onDismissRequest() }
                )
                Text(
                    text = stringResource(id = R.string.done).uppercase(),
                    color = Color.Green,
                    style = MaterialTheme.typography.body1,
                    modifier = Modifier.noRippleClickable {
                        onDone(startDate.value, endDate.value)
                        onDismissRequest()
                    }
                )
            }
        }
    }
}

private fun showDatePicker(
    context: Context,
    onDateSelected: (LocalDate) -> Unit
) {
    val localDate = LocalDate.now()
    val datePickerDialog = DatePickerDialog(
        context, { _, year, month, dayOfMonth ->
            // Month start from 0 to 11
            onDateSelected(LocalDate.of(year, month + 1, dayOfMonth))
        }, localDate.year,
        // Month start from 1 to 12
        localDate.monthValue - 1, localDate.dayOfMonth
    )
    datePickerDialog.show()
}