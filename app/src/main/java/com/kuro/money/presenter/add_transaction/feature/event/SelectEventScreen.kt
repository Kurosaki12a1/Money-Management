package com.kuro.money.presenter.add_transaction.feature.event

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.kuro.money.R
import com.kuro.money.data.model.EventEntity
import com.kuro.money.data.utils.Resource
import com.kuro.money.extension.detectHorizontalWithDelay
import com.kuro.money.navigation.routes.NavigationRoute
import com.kuro.money.presenter.add_transaction.AddTransactionViewModel
import com.kuro.money.presenter.home.feature.EditTransactionDetailViewModel
import com.kuro.money.presenter.utils.CrossSlide
import com.kuro.money.presenter.utils.popBackStackWithLifeCycle
import com.kuro.money.presenter.utils.toPainterResource
import com.kuro.money.ui.theme.Gray
import com.kuro.money.ui.theme.Green
import kotlinx.coroutines.flow.collectLatest
import java.time.LocalDate

@Composable
fun SelectEventScreen(
    navController: NavController,
    addTransactionViewModel: AddTransactionViewModel,
    selectEventViewModel: SelectEventViewModel = hiltViewModel()
) {
    BackHandler { navController.popBackStackWithLifeCycle() }

    if (navController.currentDestination?.route == NavigationRoute.AddTransaction.SelectEvent.route) {
        selectEventViewModel.getAllEvents()
    }

    val listEvent = remember { mutableListOf<EventEntity>() }
    val selectedTabIndexed = remember { mutableIntStateOf(0) }

    LaunchedEffect(Unit) {
        selectEventViewModel.getAllEvents.collectLatest {
            if (it is Resource.Success) {
                listEvent.clear()
                it.value?.let { item -> listEvent.addAll(item) }
            }
        }
    }


    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .align(Alignment.TopCenter)
            ) {
                ToolbarSelectEventScreen(navController)
                TabSelectionEvent(selectedTabIndexed.intValue) {
                    selectedTabIndexed.intValue = it
                }
                CrossSlide(
                    modifier = Modifier.detectHorizontalWithDelay(
                        onSwipeLeft = {
                            if (selectedTabIndexed.intValue < 1) {
                                selectedTabIndexed.intValue += 1
                            }
                        },
                        onSwipeRight = {
                            if (selectedTabIndexed.intValue > 0) {
                                selectedTabIndexed.intValue -= 1
                            }
                        }
                    ),
                    currentState = 0,
                    targetState = selectedTabIndexed.intValue,
                    orderedContent = listOf(0, 1)
                ) {
                    when (it) {
                        0 -> {
                            ListEventScreen(
                                navController,
                                listEvent.filter { item -> item.endDate.isAfter(LocalDate.now()) },
                                addTransactionViewModel
                            )
                        }

                        else -> {
                            ListEventScreen(
                                navController,
                                listEvent.filter { item -> item.endDate.isBefore(LocalDate.now()) },
                                addTransactionViewModel
                            )
                        }
                    }
                }
            }

            FloatingActionButton(
                onClick = {
                    navController.navigate(NavigationRoute.AddTransaction.AddEvent.route)
                },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(20.dp),
                backgroundColor = Green
            ) {
                Icon(
                    imageVector = Icons.Filled.AddCircle,
                    contentDescription = "Add",
                    tint = Color.White,
                    modifier = Modifier.size(36.dp)
                )
            }
        }
    }
}

@Composable
fun SelectEventScreen(
    navController: NavController,
    editTransactionDetailViewModel: EditTransactionDetailViewModel,
    selectEventViewModel: SelectEventViewModel = hiltViewModel()
) {
    BackHandler { navController.popBackStackWithLifeCycle() }

    if (navController.currentDestination?.route == NavigationRoute.Home.TransactionDetails.Edit.SelectEvent.route) {
        selectEventViewModel.getAllEvents()
    }

    val listEvent = remember { mutableListOf<EventEntity>() }
    val selectedTabIndexed = remember { mutableIntStateOf(0) }

    LaunchedEffect(Unit) {
        selectEventViewModel.getAllEvents.collectLatest {
            if (it is Resource.Success) {
                listEvent.clear()
                it.value?.let { item -> listEvent.addAll(item) }
            }
        }
    }


    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .align(Alignment.TopCenter)
            ) {
                ToolbarSelectEventScreen(navController)
                TabSelectionEvent(selectedTabIndexed.intValue) {
                    selectedTabIndexed.intValue = it
                }
                CrossSlide(
                    modifier = Modifier.detectHorizontalWithDelay(
                        onSwipeLeft = {
                            if (selectedTabIndexed.intValue < 1) {
                                selectedTabIndexed.intValue += 1
                            }
                        },
                        onSwipeRight = {
                            if (selectedTabIndexed.intValue > 0) {
                                selectedTabIndexed.intValue -= 1
                            }
                        }
                    ),
                    currentState = 0,
                    targetState = selectedTabIndexed.intValue,
                    orderedContent = listOf(0, 1)
                ) {
                    when (it) {
                        0 -> {
                            ListEventScreen(
                                navController,
                                listEvent.filter { item -> item.endDate.isAfter(LocalDate.now()) },
                                editTransactionDetailViewModel
                            )
                        }

                        else -> {
                            ListEventScreen(
                                navController,
                                listEvent.filter { item -> item.endDate.isBefore(LocalDate.now()) },
                                editTransactionDetailViewModel
                            )
                        }
                    }
                }
            }

            FloatingActionButton(
                onClick = {
                    navController.navigate(NavigationRoute.AddTransaction.AddEvent.route)
                },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(20.dp),
                backgroundColor = Green
            ) {
                Icon(
                    imageVector = Icons.Filled.AddCircle,
                    contentDescription = "Add",
                    tint = Color.White,
                    modifier = Modifier.size(36.dp)
                )
            }
        }
    }
}

@Composable
private fun ListEventScreen(
    navController: NavController,
    event: List<EventEntity>,
    addTransactionViewModel: AddTransactionViewModel
) {
    //TODO show the blank event screen
    if (event.isEmpty()) return
    val selectedEvent = addTransactionViewModel.eventSelected.collectAsState().value
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        items(event) {
            val isSelected = selectedEvent?.id == it.id
            Row(
                modifier = Modifier
                    .padding(horizontal = 10.dp)
                    .clickable {
                        addTransactionViewModel.setEventSelected(it.event)
                        navController.popBackStackWithLifeCycle()
                    },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(15.dp)
            ) {
                Image(painter = it.icon.toPainterResource(), contentDescription = it.name)
                Column {
                    Text(
                        text = it.name,
                        color = Color.Black,
                        style = MaterialTheme.typography.body1
                    )
                    Text(
                        text = it.wallet.balance.toString(),
                        color = Color.Black.copy(alpha = 0.5f),
                        style = MaterialTheme.typography.body2
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
                if (isSelected) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_check_green),
                        tint = Green,
                        contentDescription = "Check"
                    )
                }
            }
        }
    }
}


@Composable
private fun ListEventScreen(
    navController: NavController,
    event: List<EventEntity>,
    editTransactionDetailViewModel: EditTransactionDetailViewModel
) {
    //TODO show the blank event screen
    if (event.isEmpty()) return
    val selectedEvent = editTransactionDetailViewModel.eventSelected.collectAsState().value
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        items(event) {
            val isSelected = selectedEvent?.id == it.id
            Row(
                modifier = Modifier
                    .padding(horizontal = 10.dp)
                    .clickable {
                        editTransactionDetailViewModel.setEventSelected(it.event)
                        navController.popBackStackWithLifeCycle()
                    },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(15.dp)
            ) {
                Image(painter = it.icon.toPainterResource(), contentDescription = it.name)
                Column {
                    Text(
                        text = it.name,
                        color = Color.Black,
                        style = MaterialTheme.typography.body1
                    )
                    Text(
                        text = it.wallet.balance.toString(),
                        color = Color.Black.copy(alpha = 0.5f),
                        style = MaterialTheme.typography.body2
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
                if (isSelected) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_check_green),
                        tint = Green,
                        contentDescription = "Check"
                    )
                }
            }
        }
    }
}

@Composable
private fun ToolbarSelectEventScreen(
    navController: NavController
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back",
            modifier = Modifier.clickable { navController.popBackStackWithLifeCycle() })

        Text(
            text = stringResource(id = R.string.select_event),
            style = MaterialTheme.typography.h6,
            color = Color.Black
        )
    }
    Divider(
        modifier = Modifier
            .fillMaxWidth()
            .height(1.dp)
            .background(Gray)
    )
}

@Composable
private fun TabSelectionEvent(selectedTabIndexed: Int, onClick: (Int) -> Unit) {
    val tabTitles =
        listOf(stringResource(id = R.string.running), stringResource(id = R.string.finished))
    TabRow(selectedTabIndex = selectedTabIndexed,
        backgroundColor = Color.White,
        indicator = { tabPosition ->
            Box(
                modifier = Modifier
                    .tabIndicatorOffset(tabPosition[selectedTabIndexed])
                    .background(color = Green, RoundedCornerShape(8.dp))
                    .height(2.dp)
            )
        }) {
        tabTitles.forEachIndexed { index, title ->
            val isSelected = selectedTabIndexed == index
            Tab(selected = isSelected, onClick = { onClick(index) }, text = {
                Text(
                    text = title,
                    style = MaterialTheme.typography.body1,
                    color = if (isSelected) Color.Black else Color.Black.copy(alpha = 0.3f)
                )
            })
        }
    }
}