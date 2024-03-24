package com.kuro.money.presenter.add_transaction

import android.app.DatePickerDialog
import android.content.Context
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Checkbox
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Alarm
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.EditCalendar
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Notes
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.QuestionMark
import androidx.compose.material.icons.filled.Wallet
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalTextInputService
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.kuro.money.R
import com.kuro.money.data.model.AccountEntity
import com.kuro.money.domain.model.ScreenSelection
import com.kuro.money.domain.model.SelectedCategory
import com.kuro.money.extension.noRippleClickable
import com.kuro.money.extension.randomColor
import com.kuro.money.presenter.add_transaction.feature.note.NoteScreen
import com.kuro.money.presenter.add_transaction.feature.people.SelectPeopleScreen
import com.kuro.money.presenter.add_transaction.feature.select_category.SelectCategoryScreen
import com.kuro.money.presenter.add_transaction.feature.wallet.SelectWalletScreen
import com.kuro.money.presenter.main.MainViewModel
import com.kuro.money.presenter.utils.CrossSlide
import com.kuro.money.presenter.utils.CustomKeyBoard
import com.kuro.money.presenter.utils.SlideUpContent
import com.kuro.money.presenter.utils.TextFieldValueUtils
import com.kuro.money.presenter.utils.evaluateExpression
import com.kuro.money.presenter.utils.toPainterResource
import com.kuro.money.ui.theme.Gray
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun AddTransactionScreen(
    mainViewModel: MainViewModel = viewModel(),
    addTransactionViewModel: AddTransactionViewModel = viewModel()
) {
    val isEnabledCustomKeyBoard = remember { mutableStateOf(false) }

    BackHandler(mainViewModel.navigateScreenTo.collectAsState().value == ScreenSelection.ADD_TRANSACTION_SCREEN) {
        if (isEnabledCustomKeyBoard.value) {
            isEnabledCustomKeyBoard.value = false
            return@BackHandler
        }
        mainViewModel.setOpenAddTransactionScreen(false)
    }

    LaunchedEffect(mainViewModel.navigateScreenTo.collectAsState().value) {
        mainViewModel.navigateScreenTo.collectLatest {
            if (it != ScreenSelection.ADD_TRANSACTION_SCREEN) {
                addTransactionViewModel.clearData()
            }
        }
    }

    val amountFieldValue = remember { mutableStateOf(TextFieldValue()) }
    val shakeEnabled = remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    val enableChildScreen = addTransactionViewModel.enableChildScreen.collectAsState().value
    val selectedCategory = addTransactionViewModel.selectedCategory.collectAsState().value
    val date = remember { mutableStateOf(LocalDate.now()) }
    val note = addTransactionViewModel.note.collectAsState().value
    val wallet = addTransactionViewModel.wallet.collectAsState().value
    val peopleSelected = addTransactionViewModel.nameOfPeople.collectAsState().value
    val context = LocalContext.current

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(Gray)
            .pointerInput(Unit) {
                detectTapGestures(onTap = {
                    if (isEnabledCustomKeyBoard.value) {
                        isEnabledCustomKeyBoard.value = false
                    }
                })
            },
    ) {
        val maxHeight = this.maxHeight
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            ToolbarAddTransaction(mainViewModel)
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(maxHeight * 0.8f)
                    .background(Gray),
                verticalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                item {
                    BodyAddTransaction(amountFieldValue,
                        selectedCategory,
                        note,
                        date.value,
                        wallet,
                        onAmountClick = { isEnabledCustomKeyBoard.value = true },
                        onSelectCategoryClick = {
                            addTransactionViewModel.setEnableCategoryScreen(true)
                        },
                        onNoteClick = { addTransactionViewModel.setEnableNoteScreen(true) },
                        onDateClick = {
                            showDatePicker(context) {
                                date.value = it
                            }
                        },
                        onWalletClick = { addTransactionViewModel.setEnableWalletScreen(true) })
                }
                item {
                    MoreDetailsTransaction(
                        peopleSelected,
                        onSelectPeopleClick = {
                            addTransactionViewModel.setEnableSelectPeopleScreen(true)
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
                    .background(Color.White.copy(0.5f), RoundedCornerShape(16.dp)),
            ) {
                Text(
                    modifier = Modifier.align(Alignment.Center),
                    text = stringResource(id = R.string.save),
                    textAlign = TextAlign.Center,
                    color = Color.Black.copy(alpha = 0.7f)
                )
            }
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.3f)
                .align(Alignment.BottomCenter)
        ) {
            SlideUpContent(
                isVisible = isEnabledCustomKeyBoard.value
            ) {
                CustomKeyBoard(
                    onClear = { TextFieldValueUtils.clear(amountFieldValue) },
                    onBack = { TextFieldValueUtils.deleteAt(amountFieldValue) },
                    onInput = { TextFieldValueUtils.add(amountFieldValue, it) },
                    onConfirm = {
                        val value = evaluateExpression(amountFieldValue.value.text)
                        if (value.isNaN()) {
                            scope.launch {
                                shakeEnabled.value = true
                                delay(500)
                                shakeEnabled.value = false
                            }
                        } else {
                            TextFieldValueUtils.set(amountFieldValue, value.toString())
                        }
                    },
                    // Wrong expression start shaking
                    shakeEnabled = shakeEnabled.value
                )
            }
        }
    }

    CrossSlide(
        currentState = ScreenSelection.ADD_TRANSACTION_SCREEN,
        targetState = enableChildScreen,
        orderedContent = listOf(
            ScreenSelection.ADD_TRANSACTION_SCREEN,
            ScreenSelection.NOTE_SCREEN,
            ScreenSelection.SELECT_CATEGORY_SCREEN,
            ScreenSelection.WALLET_SCREEN,
            ScreenSelection.WITH_SCREEN
        )
    ) {
        when (it) {
            ScreenSelection.SELECT_CATEGORY_SCREEN -> SelectCategoryScreen()
            ScreenSelection.NOTE_SCREEN -> NoteScreen()
            ScreenSelection.WALLET_SCREEN -> SelectWalletScreen()
            ScreenSelection.WITH_SCREEN -> SelectPeopleScreen()
            else -> {}
        }
    }
}

private fun showDatePicker(
    context: Context,
    onDateSelected: (LocalDate) -> Unit,
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

@Composable
private fun MoreDetailsTransaction(
    peopleSelected: String?,
    onSelectPeopleClick: () -> Unit
) {
    /**
     * Add people
     */
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(30.dp),
            modifier = Modifier
                .padding(20.dp)
                .clickable { onSelectPeopleClick() }) {
            Icon(
                imageVector = Icons.Default.People,
                contentDescription = "People",
                modifier = Modifier.size(30.dp)
            )
            if (peopleSelected == null) {
                Text(text = stringResource(id = R.string.with))
            } else {
                val listPeople = peopleSelected.split(",")
                val listRandomColor = listPeople.map { randomColor() }
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(5.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    itemsIndexed(listPeople) { index, item ->
                        Row(
                            modifier = Modifier
                                .background(Gray, RoundedCornerShape(16.dp))
                                .padding(end = 10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .background(listRandomColor[index], CircleShape)
                                    .size(30.dp)
                            ) {
                                Text(
                                    text = item[0].toString().uppercase(),
                                    color = Color.White,
                                    style = MaterialTheme.typography.body1,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.align(Alignment.Center)
                                )
                            }
                            Text(
                                text = item, color = Color.Black,
                                style = MaterialTheme.typography.body1,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
        }
    }

    Spacer(modifier = Modifier.height(20.dp))

    /**
     * Add event, remind
     */
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(30.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Filled.Event,
                    contentDescription = "Event",
                    tint = Color.Black,
                )
                Text(text = stringResource(id = R.string.select_event),
                    style = MaterialTheme.typography.body1,
                    color = Color.Black.copy(alpha = 0.7f),
                    modifier = Modifier.clickable { })
            }
            Row(
                horizontalArrangement = Arrangement.spacedBy(20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Filled.Alarm,
                    contentDescription = "Alarm",
                    tint = Color.Black,
                )
                Text(text = stringResource(id = R.string.no_remind),
                    style = MaterialTheme.typography.body1,
                    color = Color.Black.copy(alpha = 0.7f),
                    modifier = Modifier.clickable { })
            }
        }
    }

    Spacer(modifier = Modifier.height(20.dp))

    /**
     * Add picture and camera
     */
    BoxWithConstraints(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .background(Color.White)
            .padding(10.dp)
    ) {
        Icon(
            modifier = Modifier
                .width(this.maxWidth * 0.5f)
                .align(Alignment.CenterStart)
                .noRippleClickable { },
            imageVector = Icons.Default.Image,
            tint = Color.Black,
            contentDescription = "Image"
        )
        Divider(
            modifier = Modifier
                .width(1.dp)
                .height(50.dp)
                .background(Gray)
                .align(Alignment.Center)
        )
        Icon(
            modifier = Modifier
                .width(this.maxWidth * 0.5f)
                .align(Alignment.CenterEnd)
                .noRippleClickable { },
            imageVector = Icons.Default.PhotoCamera,
            tint = Color.Black,
            contentDescription = "Camera"
        )
    }

    /**
     * Exclude from report option
     */
    val isCheckedBox = remember { mutableStateOf(false) }

    Spacer(modifier = Modifier.height(20.dp))

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 30.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Checkbox(checked = isCheckedBox.value, onCheckedChange = { isCheckedBox.value = it })
            Column(
                modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.excluded_report),
                    style = MaterialTheme.typography.body1,
                    color = Color.Black
                )
                Text(
                    text = stringResource(id = R.string.exclude_report_detail),
                    style = MaterialTheme.typography.body2,
                    color = Color.Black.copy(alpha = 0.7f)
                )
            }
        }

    }
}


@Composable
private fun ToolbarAddTransaction(
    mainViewModel: MainViewModel
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .background(Color.White)
    ) {
        Row(
            modifier = Modifier.padding(20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(30.dp)
        ) {
            Icon(imageVector = Icons.Filled.Close,
                contentDescription = "Close",
                tint = Color.Black,
                modifier = Modifier.clickable {
                    mainViewModel.setOpenAddTransactionScreen(false)
                })
            Text(
                text = stringResource(id = R.string.add_transaction),
                style = MaterialTheme.typography.h6,
                color = Color.Black
            )
        }
    }
}

@Composable
private fun BodyAddTransaction(
    amount: MutableState<TextFieldValue>,
    selectedCategory: SelectedCategory,
    note: String,
    date: LocalDate,
    wallet: AccountEntity?,
    onAmountClick: () -> Unit,
    onSelectCategoryClick: () -> Unit,
    onNoteClick: () -> Unit,
    onDateClick: () -> Unit,
    onWalletClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }.also {
        val isPressed by it.collectIsPressedAsState()
        if (isPressed) onAmountClick()
    }
    val textTime = if (date == LocalDate.now()) {
        stringResource(id = R.string.today)
    } else {
        DateTimeFormatter.ofPattern("dd/MM/yyyy").format(date)
    }

    Surface(
        modifier = Modifier.fillMaxWidth(), color = Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 10.dp),
            verticalArrangement = Arrangement.spacedBy(30.dp)
        ) {
            CompositionLocalProvider(LocalTextInputService provides null) {
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = amount.value,
                    interactionSource = interactionSource,
                    onValueChange = { amount.value = it },
                    singleLine = true,
                    label = { Text(text = stringResource(id = R.string.amount)) },
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onSelectCategoryClick() },
                horizontalArrangement = Arrangement.spacedBy(20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (selectedCategory.name != "" && selectedCategory.icon != "") {
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
                    .clickable { onDateClick() },
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