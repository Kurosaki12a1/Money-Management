package com.kuro.money.presenter.add_transaction

import android.Manifest
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Checkbox
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Alarm
import androidx.compose.material.icons.filled.Clear
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.kuro.money.R
import com.kuro.money.data.model.Event
import com.kuro.money.data.utils.FileUtils
import com.kuro.money.data.utils.Resource
import com.kuro.money.domain.model.LetterColor
import com.kuro.money.extension.noRippleClickable
import com.kuro.money.navigation.routes.NavigationRoute.AddTransaction
import com.kuro.money.presenter.utils.DecimalFormatter
import com.kuro.money.presenter.utils.DecimalInputVisualTransformation
import com.kuro.money.presenter.utils.popBackStackWithLifeCycle
import com.kuro.money.presenter.utils.string
import com.kuro.money.presenter.utils.toPainterResource
import com.kuro.money.ui.theme.Gray
import com.kuro.money.ui.theme.Green
import kotlinx.coroutines.flow.collectLatest
import java.time.LocalDate
import java.time.format.DateTimeFormatter


@Composable
fun AddTransactionScreen(
    navController: NavController,
    addTransactionViewModel: AddTransactionViewModel
) {
    BackHandler { navController.popBackStackWithLifeCycle() }

    val selectedCategory = addTransactionViewModel.selectedCategory.collectAsState().value
    val selectedCurrency = addTransactionViewModel.currencySelected.collectAsState().value
    val amount = addTransactionViewModel.amount.collectAsState().value
    val wallet = addTransactionViewModel.wallet.collectAsState().value
    val peopleSelected = addTransactionViewModel.nameOfPeople.collectAsState().value
    val eventSelected = addTransactionViewModel.eventSelected.collectAsState().value
    val imageSelectedFromGallery = addTransactionViewModel.uriSelected.collectAsState().value
    val imageFromCamera = addTransactionViewModel.bitmapFlow.collectAsState().value

    val dateTransaction = addTransactionViewModel.dateTransaction.collectAsState().value
    val dateRemind = addTransactionViewModel.dateRemind.collectAsState().value

    val context = LocalContext.current
    val shouldSubmitTransaction = remember { mutableStateOf(false) }

    LaunchedEffect(amount, selectedCategory, dateTransaction, wallet, selectedCurrency) {
        shouldSubmitTransaction.value = !(amount == null
                || wallet == null
                || selectedCurrency == null
                || selectedCategory == null)
    }

    LaunchedEffect(Unit) {
        addTransactionViewModel.insertTransaction.collectLatest {
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
                        addTransactionViewModel,
                        onCurrencyClick = {
                            navController.navigate(AddTransaction.SelectCurrency.route)
                        },
                        onSelectCategoryClick = {
                            navController.navigate(AddTransaction.SelectCategory.route)
                        },
                        onNoteClick = {
                            navController.navigate(AddTransaction.Note.route)
                        },
                        onWalletClick = {
                            navController.navigate(AddTransaction.SelectWallet.route)
                        })
                }
                item {
                    MoreDetailsTransaction(
                        peopleSelected,
                        eventSelected,
                        dateRemind,
                        imageSelectedFromGallery,
                        imageFromCamera,
                        onSelectPeopleClick = { navController.navigate(AddTransaction.With.route) },
                        onSelectEventClick = {
                            navController.navigate(AddTransaction.SelectEvent.route)
                        },
                        onAlarmClick = {
                            showDatePicker(context, true) {
                                addTransactionViewModel.setDateRemind(it)
                            }
                        },
                        onImagePicked = {
                            addTransactionViewModel.setUriSelected(it)
                        },
                        onCameraPicked = {
                            addTransactionViewModel.setBitmap(it)
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
                            addTransactionViewModel.setUriSelected(
                                FileUtils.getUriForFile(
                                    context, FileUtils.saveBitmapToFile(
                                        context, imageFromCamera
                                    )
                                )
                            )
                        }
                        if (shouldSubmitTransaction.value) {
                            addTransactionViewModel.setDefaultCurrency()
                            addTransactionViewModel.submitData()
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

fun showDatePicker(
    context: Context,
    shouldEnableMinDate: Boolean = false,
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
    if (shouldEnableMinDate) datePickerDialog.datePicker.minDate = System.currentTimeMillis()
    datePickerDialog.show()
}

@Composable
fun MoreDetailsTransaction(
    peopleSelected: String?,
    eventSelected: Event?,
    dateRemind: LocalDate?,
    uriSelected: Uri?,
    imageFromCamera: Bitmap?,
    onSelectPeopleClick: () -> Unit,
    onSelectEventClick: () -> Unit,
    onAlarmClick: () -> Unit,
    onImagePicked: (Uri?) -> Unit,
    onCameraPicked: (Bitmap?) -> Unit
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
                .fillMaxWidth()
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
                val listPeople =
                    peopleSelected.split(",").filter { it.isNotEmpty() }.map { it.trim() }
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(5.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    itemsIndexed(listPeople) { _, item ->
                        Row(
                            modifier = Modifier
                                .background(Gray, RoundedCornerShape(16.dp))
                                .padding(end = 10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .background(LetterColor.getColor(item[0]), CircleShape)
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
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onSelectEventClick() },
                horizontalArrangement = Arrangement.spacedBy(20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (eventSelected == null) {
                    Icon(
                        imageVector = Icons.Filled.Event,
                        contentDescription = "Event",
                        tint = Color.Black,
                    )
                    Text(
                        text = stringResource(id = R.string.select_event),
                        style = MaterialTheme.typography.body1,
                        color = Color.Black.copy(alpha = 0.7f),
                    )
                } else {
                    Image(
                        painter = eventSelected.icon.toPainterResource(),
                        contentDescription = "Event",
                    )
                    Text(
                        text = eventSelected.name,
                        style = MaterialTheme.typography.body1,
                        color = Color.Black.copy(alpha = 0.7f),
                    )
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onAlarmClick() },
                horizontalArrangement = Arrangement.spacedBy(20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Filled.Alarm,
                    contentDescription = "Alarm",
                    tint = Color.Black,
                )
                if (dateRemind == null) {
                    Text(
                        text = stringResource(id = R.string.no_remind),
                        style = MaterialTheme.typography.body1,
                        color = Color.Black.copy(alpha = 0.7f),
                    )
                } else {
                    Text(
                        text = dateRemind.string(),
                        style = MaterialTheme.typography.body1,
                        color = Color.Black.copy(alpha = 0.7f),
                    )
                }

            }
        }
    }

    Spacer(modifier = Modifier.height(20.dp))

    /**
     * Add picture and camera
     */
    /*val launcherChooseImage =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) {
            onImagePicked(it)
        }*/
    val context = LocalContext.current
    val launcherChooseCamera =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.TakePicturePreview()) {
            onCameraPicked(it)
        }
    val launcherRequestPermission =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.OpenDocument()) { result ->
            result?.let {
                context.contentResolver.takePersistableUriPermission(
                    it,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION
                )
            }
            onImagePicked(result)
        }
    val launcherRequestCameraPermission =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.RequestPermission()) {
            if (it) {
                launcherChooseCamera.launch(null)
            }
        }
    BoxWithConstraints(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(10.dp)
    ) {
        if (uriSelected == null && imageFromCamera == null) {
            Icon(
                modifier = Modifier
                    .width(this.maxWidth * 0.5f)
                    .align(Alignment.CenterStart)
                    .noRippleClickable {
                        launcherRequestPermission.launch(arrayOf("image/*"))
                    },
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
                    .noRippleClickable { launcherRequestCameraPermission.launch(Manifest.permission.CAMERA) },
                imageVector = Icons.Default.PhotoCamera,
                tint = Color.Black,
                contentDescription = "Camera"
            )
        } else if (uriSelected != null) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(uriSelected)
                    .build(),
                contentDescription = "Photo",
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.Center),
                contentScale = ContentScale.Crop
            )
            Box(
                modifier = Modifier
                    .size(30.dp)
                    .align(Alignment.TopEnd)
                    .background(Gray, CircleShape)
                    .noRippleClickable { onImagePicked(null) }
            ) {
                Icon(
                    imageVector = Icons.Default.Clear,
                    contentDescription = "Clear",
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        } else {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(imageFromCamera)
                    .build(),
                contentDescription = "Photo",
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.Center),
                contentScale = ContentScale.Crop
            )
            Box(
                modifier = Modifier
                    .size(30.dp)
                    .align(Alignment.TopEnd)
                    .background(Gray, CircleShape)
                    .noRippleClickable { onCameraPicked(null) }
            ) {
                Icon(
                    imageVector = Icons.Default.Clear,
                    contentDescription = "Clear",
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
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
fun ToolbarAddTransaction(
    navController: NavController
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
                modifier = Modifier.clickable { navController.popBackStackWithLifeCycle() })
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
    addTransactionViewModel: AddTransactionViewModel,
    onCurrencyClick: () -> Unit,
    onSelectCategoryClick: () -> Unit,
    onNoteClick: () -> Unit,
    onWalletClick: () -> Unit
) {
    val context = LocalContext.current
    val selectedCategory = addTransactionViewModel.selectedCategory.collectAsState().value
    val selectedCurrency = addTransactionViewModel.currencySelected.collectAsState().value
    val amount = addTransactionViewModel.amount.collectAsState().value
    val note = addTransactionViewModel.note.collectAsState().value
    val wallet = addTransactionViewModel.wallet.collectAsState().value
    val dateTransaction = addTransactionViewModel.dateTransaction.collectAsState().value

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
                        addTransactionViewModel.setAmount(decimalFormatter.cleanup(it))
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
                if (selectedCategory != null
                    && selectedCategory.name != ""
                    && selectedCategory.icon != ""
                ) {
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
                            addTransactionViewModel.setDateTransaction(it)
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