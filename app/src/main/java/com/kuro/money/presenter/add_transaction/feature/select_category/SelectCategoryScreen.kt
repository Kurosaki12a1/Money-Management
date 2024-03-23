package com.kuro.money.presenter.add_transaction.feature.select_category

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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.kuro.money.R
import com.kuro.money.data.model.CategoryEntity
import com.kuro.money.data.utils.Resource
import com.kuro.money.domain.model.ScreenSelection
import com.kuro.money.domain.model.SelectedCategory
import com.kuro.money.extension.detectHorizontalWithDelay
import com.kuro.money.presenter.add_transaction.AddTransactionViewModel
import com.kuro.money.presenter.add_transaction.feature.select_category.feature.DebtScreen
import com.kuro.money.presenter.add_transaction.feature.select_category.feature.ExpenseScreen
import com.kuro.money.presenter.add_transaction.feature.select_category.feature.IncomeScreen
import com.kuro.money.presenter.utils.CrossSlide
import com.kuro.money.ui.theme.Gray
import kotlinx.coroutines.flow.collectLatest

@Composable
fun SelectCategoryScreen(
    addTransactionViewModel: AddTransactionViewModel = viewModel(),
    selectCategoryViewModel: SelectCategoryViewModel = viewModel()
) {

    BackHandler(enabled = addTransactionViewModel.enableChildScreen.collectAsState().value == ScreenSelection.SELECT_CATEGORY_SCREEN) {
        addTransactionViewModel.setEnableCategoryScreen(false)
    }

    val selectedTabIndexed = remember { mutableStateOf(0) }
    val prevSelectedTabIndex = remember { mutableStateOf(0) }
    val listCategories = remember { mutableStateListOf<CategoryEntity>() }
    val listSpecialCategories = listOf(
        "Debt", "Debt Collection", "Loan", "Repayment"
    )

    LaunchedEffect(selectCategoryViewModel.selectedCategory.collectAsState().value) {
        selectCategoryViewModel.selectedCategory.collectLatest {
            if (it.name != "" && it.icon != "") {
                addTransactionViewModel.setSelectedCategory(it)
                selectCategoryViewModel.setSelectedCategories(SelectedCategory("", ""))
                addTransactionViewModel.setEnableCategoryScreen(false)
            }
        }
    }

    LaunchedEffect(selectCategoryViewModel.getCategoryResponse.collectAsState().value) {
        selectCategoryViewModel.getCategoryResponse.collectLatest {
            if (it is Resource.Success) {
                listCategories.clear()
                listCategories.addAll(it.value)
            }
        }
    }

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column {
            ToolbarSelectCategory()
            TabSelectionCategory(selectedTabIndexed.value) {
                if (prevSelectedTabIndex.value != selectedTabIndexed.value) {
                    prevSelectedTabIndex.value = selectedTabIndexed.value
                }
                selectedTabIndexed.value = it
            }
            CrossSlide(
                modifier = Modifier.detectHorizontalWithDelay(onSwipeLeft = {
                    if (selectedTabIndexed.value < 2) {
                        prevSelectedTabIndex.value = selectedTabIndexed.value
                        selectedTabIndexed.value += 1
                    }
                }, onSwipeRight = {
                    if (selectedTabIndexed.value > 0) {
                        prevSelectedTabIndex.value = selectedTabIndexed.value
                        selectedTabIndexed.value -= 1
                    }
                }),
                currentState = prevSelectedTabIndex.value,
                targetState = selectedTabIndexed.value,
                orderedContent = listOf(0, 1, 2)
            ) {
                when (it) {
                    0 -> ExpenseScreen(listCategories.filter {
                        it.type == "expense" && !listSpecialCategories.contains(it.name)
                    })

                    1 -> IncomeScreen(listCategories.filter {
                        it.type == "income" && !listSpecialCategories.contains(it.name)
                    })

                    2 -> DebtScreen(listCategories.filter { listSpecialCategories.contains(it.name) })
                }
            }
        }
    }
}

@Composable
private fun TabSelectionCategory(selectedTabIndexed: Int, onClick: (Int) -> Unit) {
    val tabTitles = listOf(
        stringResource(id = R.string.expense),
        stringResource(id = R.string.income),
        stringResource(id = R.string.debt)
    )
    TabRow(selectedTabIndex = selectedTabIndexed,
        backgroundColor = Color.White,
        indicator = { tabPosition ->
            Box(
                modifier = Modifier
                    .tabIndicatorOffset(tabPosition[selectedTabIndexed])
                    .background(color = Color.Black, RoundedCornerShape(8.dp))
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

@Composable
private fun ToolbarSelectCategory(
    addTransactionViewModel: AddTransactionViewModel = viewModel()
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(30.dp)
    ) {
        Icon(imageVector = Icons.Filled.ArrowBack,
            contentDescription = "Close",
            tint = Color.Black,
            modifier = Modifier.clickable {
                addTransactionViewModel.setEnableCategoryScreen(false)
            })
        Text(
            text = stringResource(id = R.string.select_category),
            style = MaterialTheme.typography.h6,
            color = Color.Black
        )
        Spacer(modifier = Modifier.weight(1f))
        Icon(
            imageVector = Icons.Default.Search, contentDescription = "Search"
        )
    }
    Divider(
        modifier = Modifier
            .fillMaxWidth()
            .height(1.dp)
            .background(Gray)
    )
}