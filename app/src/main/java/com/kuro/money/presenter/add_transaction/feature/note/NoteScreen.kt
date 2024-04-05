package com.kuro.money.presenter.add_transaction.feature.note

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.kuro.money.R
import com.kuro.money.presenter.add_transaction.AddTransactionViewModel
import com.kuro.money.presenter.home.feature.EditTransactionDetailViewModel
import com.kuro.money.ui.theme.Gray

@Composable
fun NoteScreen(
    navController: NavController,
    addTransactionViewModel: AddTransactionViewModel
) {
    BackHandler { navController.popBackStack() }

    val noteValue = addTransactionViewModel.note.collectAsState().value
    val note = remember { mutableStateOf(noteValue) }
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                Icon(imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    modifier = Modifier.clickable {
                        navController.popBackStack()
                    })
                Text(
                    text = stringResource(id = R.string.note),
                    style = MaterialTheme.typography.h6,
                    textAlign = TextAlign.Center,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(text = stringResource(id = R.string.save),
                    style = MaterialTheme.typography.h6,
                    color = Color.Black,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.clickable {
                        addTransactionViewModel.setNote(note.value)
                        navController.popBackStack()
                    })
            }
            Divider(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(Gray)
            )
            BasicTextField(modifier = Modifier
                .fillMaxSize()
                .focusRequester(focusRequester)
                .padding(10.dp),
                value = note.value,
                textStyle = TextStyle(
                    fontSize = 18.sp
                ),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Ascii),
                onValueChange = { note.value = it })
        }
    }
}

@Composable
fun NoteScreen(
    navController: NavController,
    editTransactionDetailViewModel: EditTransactionDetailViewModel
) {
    BackHandler { navController.popBackStack() }

    val noteValue = editTransactionDetailViewModel.note.collectAsState().value
    val note = remember { mutableStateOf(noteValue) }
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                Icon(imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    modifier = Modifier.clickable {
                        navController.popBackStack()
                    })
                Text(
                    text = stringResource(id = R.string.note),
                    style = MaterialTheme.typography.h6,
                    textAlign = TextAlign.Center,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(text = stringResource(id = R.string.save),
                    style = MaterialTheme.typography.h6,
                    color = Color.Black,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.clickable {
                        editTransactionDetailViewModel.setNote(note.value)
                        navController.popBackStack()
                    })
            }
            Divider(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(Gray)
            )
            BasicTextField(modifier = Modifier
                .fillMaxSize()
                .focusRequester(focusRequester)
                .padding(10.dp),
                value = note.value,
                textStyle = TextStyle(
                    fontSize = 18.sp
                ),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Ascii),
                onValueChange = { note.value = it })
        }
    }
}