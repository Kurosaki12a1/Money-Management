package com.kuro.money.presenter.add_transaction.feature.people

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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
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
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.kuro.money.R
import com.kuro.money.domain.model.ScreenSelection
import com.kuro.money.presenter.add_transaction.AddTransactionViewModel
import com.kuro.money.ui.theme.Gray
import com.kuro.money.ui.theme.Green

@Composable
fun SelectPeopleScreen(
    addTransactionViewModel: AddTransactionViewModel = viewModel()
) {

    BackHandler(addTransactionViewModel.enableChildScreen.collectAsState().value == ScreenSelection.WITH_SCREEN) {
        addTransactionViewModel.setEnableSelectPeopleScreen(false)
    }

    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    val nameOfPeople = addTransactionViewModel.nameOfPeople.collectAsState().value
    val name = remember {
        mutableStateOf(
            TextFieldValue(
                text = nameOfPeople ?: "",
                selection = TextRange(nameOfPeople?.length ?: 0)
            )
        )
    }

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(30.dp)
            ) {
                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back",
                    modifier = Modifier.clickable {
                        addTransactionViewModel.setEnableSelectPeopleScreen(false)
                    })
                Text(
                    text = stringResource(id = R.string.with),
                    style = MaterialTheme.typography.h6,
                    color = Color.Black,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = stringResource(id = R.string.done),
                    style = MaterialTheme.typography.body1,
                    color = Green,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.clickable {
                        name.value.let { addTransactionViewModel.setNamePeople(it.text) }
                        addTransactionViewModel.setEnableSelectPeopleScreen(false)
                    }
                )
            }
            Divider(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(Gray)
            )
            BasicTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp)
                    .focusRequester(focusRequester)
                    .offset(x = 50.dp),
                value = name.value,
                onValueChange = { name.value = it },
                textStyle = TextStyle(
                    fontSize = 18.sp,
                    color = Color.Black
                )
            )
        }
    }
}