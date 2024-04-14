package com.kuro.money.presenter.add_transaction.feature.select_category.feature

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.kuro.money.R
import com.kuro.money.data.model.CategoryEntity
import com.kuro.money.presenter.add_transaction.AddTransactionViewModel
import com.kuro.money.presenter.add_transaction.feature.select_category.SelectCategoryViewModel
import com.kuro.money.presenter.home.feature.EditTransactionDetailViewModel
import com.kuro.money.presenter.utils.toPainterResource
import com.kuro.money.ui.theme.Gray

@Composable
fun DebtScreen(
    listCategory: List<CategoryEntity>,
    addTransactionViewModel: AddTransactionViewModel,
    vm: SelectCategoryViewModel = hiltViewModel(),
) {
    val newListCategory = mutableListOf<CategoryEntity>()
    listCategory.forEach { category ->
        category.subCategories = category.subCategories.sortedBy { it.name }.toMutableList()
    }
    newListCategory.addAll(listCategory.sortedBy { it.name })

    val selectedCategory = addTransactionViewModel.selectedCategory.collectAsState().value

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 10.dp)
        ) {
            itemsIndexed(newListCategory, key = { _, item -> item.name }) { index, item ->
                val isLastIndex = index == newListCategory.size - 1
                CategoryItem(item, isLastIndex, selectedCategory) {
                    vm.setSelectedCategories(it)
                }
            }
        }
    }
}

@Composable
fun DebtScreen(
    listCategory: List<CategoryEntity>,
    editTransactionViewModel: EditTransactionDetailViewModel,
    vm: SelectCategoryViewModel = hiltViewModel(),
) {
    val newListCategory = mutableListOf<CategoryEntity>()
    listCategory.forEach { category ->
        category.subCategories = category.subCategories.sortedBy { it.name }.toMutableList()
    }
    newListCategory.addAll(listCategory.sortedBy { it.name })

    val selectedCategory = editTransactionViewModel.selectedCategory.collectAsState().value

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 10.dp)
        ) {
            itemsIndexed(newListCategory, key = { _, item -> item.name }) { index, item ->
                val isLastIndex = index == newListCategory.size - 1
                CategoryItem(item, isLastIndex, selectedCategory) {
                    vm.setSelectedCategories(it)
                }
            }
        }
    }
}

@Composable
private fun CategoryItem(
    entity: CategoryEntity,
    isLastIndexCategory: Boolean,
    categorySelected: CategoryEntity?,
    onClick: (CategoryEntity) -> Unit
) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
                .clickable { onClick(entity) },
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = entity.icon.toPainterResource(),
                contentDescription = entity.title,
            )
            Text(
                text = entity.name, style = MaterialTheme.typography.body1, color = Color.Black
            )
            if (categorySelected?.name == entity.name) {
                Spacer(modifier = Modifier.weight(1f))
                Image(
                    painter = painterResource(id = R.drawable.ic_check_green),
                    contentDescription = "Check"
                )
            }
        }
        if (!isLastIndexCategory) {
            Divider(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(Gray.copy(alpha = 0.1f))
            )
        }
    }
}