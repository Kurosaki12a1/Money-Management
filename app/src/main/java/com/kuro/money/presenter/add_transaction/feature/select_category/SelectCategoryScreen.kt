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
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.kuro.money.R
import com.kuro.money.data.model.CategoryEntity
import com.kuro.money.data.utils.Resource
import com.kuro.money.extension.detectHorizontalWithDelay
import com.kuro.money.presenter.add_transaction.AddTransactionViewModel
import com.kuro.money.presenter.add_transaction.feature.select_category.feature.DebtScreen
import com.kuro.money.presenter.add_transaction.feature.select_category.feature.ExpenseScreen
import com.kuro.money.presenter.add_transaction.feature.select_category.feature.IncomeScreen
import com.kuro.money.presenter.home.feature.EditTransactionDetailViewModel
import com.kuro.money.presenter.utils.CrossSlide
import com.kuro.money.presenter.utils.popBackStackWithLifeCycle
import com.kuro.money.ui.theme.Gray
import kotlinx.coroutines.flow.collectLatest

@Composable
fun SelectCategoryScreen(
    navController: NavController,
    addTransactionViewModel: AddTransactionViewModel,
    selectCategoryViewModel: SelectCategoryViewModel
) {
    BackHandler {
        navController.popBackStackWithLifeCycle()
    }

    val selectedTabIndexed = remember { mutableIntStateOf(0) }
    val prevSelectedTabIndex = remember { mutableIntStateOf(0) }
    val listCategories = remember { mutableStateListOf<CategoryEntity>() }
    val listSpecialCategories = listOf(
        "Debt", "Debt Collection", "Loan", "Repayment"
    )

    LaunchedEffect(Unit) {
        selectCategoryViewModel.selectedCategory.collectLatest {
            if (it != null) {
                addTransactionViewModel.setSelectedCategory(it)
                selectCategoryViewModel.setSelectedCategories(null)
                navController.popBackStackWithLifeCycle()
            }
        }
    }

    LaunchedEffect(Unit) {
        selectCategoryViewModel.getCategoryResponse.collectLatest {
            if (it is Resource.Success) {
                listCategories.clear()
                listCategories.addAll(it.value)

                val subCategoryMap =
                    listCategories.filter { i -> i.parentId != i.id }.groupBy { g -> g.parentId }
                listCategories.forEach { category ->
                    category.subCategories = subCategoryMap[category.parentId] ?: emptyList()
                }
            }
        }
    }

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column {
            ToolbarSelectCategory(navController)
            TabSelectionCategory(selectedTabIndexed.intValue) {
                if (prevSelectedTabIndex.intValue != selectedTabIndexed.intValue) {
                    prevSelectedTabIndex.intValue = selectedTabIndexed.intValue
                }
                selectedTabIndexed.intValue = it
            }
            CrossSlide(
                modifier = Modifier.detectHorizontalWithDelay(onSwipeLeft = {
                    if (selectedTabIndexed.intValue < 2) {
                        prevSelectedTabIndex.intValue = selectedTabIndexed.intValue
                        selectedTabIndexed.intValue += 1
                    }
                }, onSwipeRight = {
                    if (selectedTabIndexed.intValue > 0) {
                        prevSelectedTabIndex.intValue = selectedTabIndexed.intValue
                        selectedTabIndexed.intValue -= 1
                    }
                }),
                currentState = prevSelectedTabIndex.intValue,
                targetState = selectedTabIndexed.intValue,
                orderedContent = listOf(0, 1, 2)
            ) {
                when (it) {
                    0 -> ExpenseScreen(listCategories.filter { cate ->
                        cate.type == "expense" && !listSpecialCategories.contains(cate.name)
                    }, addTransactionViewModel, selectCategoryViewModel)

                    1 -> IncomeScreen(listCategories.filter { cate ->
                        cate.type == "income" && !listSpecialCategories.contains(cate.name)
                    }, addTransactionViewModel, selectCategoryViewModel)

                    2 -> DebtScreen(
                        listCategories.filter { cate -> listSpecialCategories.contains(cate.name) },
                        addTransactionViewModel, selectCategoryViewModel
                    )
                }
            }
        }
    }
}

@Composable
fun SelectCategoryScreen(
    navController: NavController,
    editTransactionDetailViewModel: EditTransactionDetailViewModel,
    selectCategoryViewModel: SelectCategoryViewModel
) {
    BackHandler {
        navController.popBackStackWithLifeCycle()
    }

    val selectedTabIndexed = remember { mutableIntStateOf(0) }
    val prevSelectedTabIndex = remember { mutableIntStateOf(0) }
    val listCategories = remember { mutableStateListOf<CategoryEntity>() }
    val listSpecialCategories = listOf(
        "Debt", "Debt Collection", "Loan", "Repayment"
    )

    LaunchedEffect(Unit) {
        selectCategoryViewModel.selectedCategory.collectLatest {
            if (it != null) {
                editTransactionDetailViewModel.setSelectedCategory(it)
                selectCategoryViewModel.setSelectedCategories(null)
                navController.popBackStackWithLifeCycle()
            }
        }
    }

    LaunchedEffect(Unit) {
        selectCategoryViewModel.getCategoryResponse.collectLatest {
            if (it is Resource.Success) {
                listCategories.clear()
                listCategories.addAll(it.value)

                val subCategoryMap =
                    listCategories.filter { f -> f.parentId != f.id }.groupBy { g -> g.parentId }
                listCategories.forEach { category ->
                    category.subCategories = subCategoryMap[category.parentId] ?: emptyList()
                }
            }
        }
    }

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column {
            ToolbarSelectCategory(navController)
            TabSelectionCategory(selectedTabIndexed.intValue) {
                if (prevSelectedTabIndex.intValue != selectedTabIndexed.intValue) {
                    prevSelectedTabIndex.intValue = selectedTabIndexed.intValue
                }
                selectedTabIndexed.intValue = it
            }
            CrossSlide(
                modifier = Modifier.detectHorizontalWithDelay(onSwipeLeft = {
                    if (selectedTabIndexed.intValue < 2) {
                        prevSelectedTabIndex.intValue = selectedTabIndexed.intValue
                        selectedTabIndexed.intValue += 1
                    }
                }, onSwipeRight = {
                    if (selectedTabIndexed.intValue > 0) {
                        prevSelectedTabIndex.intValue = selectedTabIndexed.intValue
                        selectedTabIndexed.intValue -= 1
                    }
                }),
                currentState = prevSelectedTabIndex.intValue,
                targetState = selectedTabIndexed.intValue,
                orderedContent = listOf(0, 1, 2)
            ) {
                when (it) {
                    0 -> ExpenseScreen(listCategories.filter { cate ->
                        cate.type == "expense" && !listSpecialCategories.contains(cate.name)
                    }, editTransactionDetailViewModel, selectCategoryViewModel)

                    1 -> IncomeScreen(listCategories.filter { cate ->
                        cate.type == "income" && !listSpecialCategories.contains(cate.name)
                    }, editTransactionDetailViewModel, selectCategoryViewModel)

                    2 -> DebtScreen(
                        listCategories.filter { cate -> listSpecialCategories.contains(cate.name) },
                        editTransactionDetailViewModel, selectCategoryViewModel
                    )
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
    navController: NavController
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
                navController.popBackStackWithLifeCycle()
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