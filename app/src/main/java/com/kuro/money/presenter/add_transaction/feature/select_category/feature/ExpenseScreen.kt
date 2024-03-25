package com.kuro.money.presenter.add_transaction.feature.select_category.feature

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.kuro.money.R
import com.kuro.money.data.model.CategoryEntity
import com.kuro.money.domain.model.SelectedCategory
import com.kuro.money.presenter.add_transaction.AddTransactionViewModel
import com.kuro.money.presenter.add_transaction.feature.select_category.SelectCategoryViewModel
import com.kuro.money.presenter.utils.toPainterResource
import com.kuro.money.ui.theme.Gray
import com.kuro.money.ui.theme.Green

@Composable
fun ExpenseScreen(
    listCategory: List<CategoryEntity>,
    vm: SelectCategoryViewModel = viewModel(),
    addTransactionViewModel: AddTransactionViewModel = viewModel(),
) {
    val newListCategory = mutableListOf<CategoryEntity>()
    val sortedNameList = listCategory.sortedBy { it.name }
    newListCategory.addAll(sortedNameList.map { parent ->
        parent.copy(subCategories = parent.subCategories.sortedBy { it.name })
    }.toCollection(ArrayList()))

    val selectedCategory = addTransactionViewModel.selectedCategory.collectAsState().value
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            item {
                Column {
                    Row(
                        modifier = Modifier.padding(10.dp),
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.AddCircle,
                            contentDescription = "Add",
                            tint = Green,
                            modifier = Modifier.size(48.dp)
                        )
                        Text(
                            text = stringResource(id = R.string.new_category),
                            style = MaterialTheme.typography.body1,
                            color = Green
                        )
                    }
                }
                Divider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(10.dp)
                        .background(Gray.copy(alpha = 0.1f))
                )
            }
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
    categorySelected: SelectedCategory,
    onClick: (SelectedCategory) -> Unit
) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
                .clickable { onClick(SelectedCategory(entity.name, entity.icon)) },
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
            if (categorySelected.name == entity.name) {
                Spacer(modifier = Modifier.weight(1f))
                Image(
                    painter = painterResource(id = R.drawable.ic_check_green),
                    contentDescription = "Check"
                )
            }
        }

        entity.subCategories.forEachIndexed { index, subCategoryEntity ->
            val isLastIndex = index == entity.subCategories.size - 1
            Row(verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp)
                    .clickable {
                        onClick(
                            SelectedCategory(
                                subCategoryEntity.name, subCategoryEntity.icon
                            )
                        )
                    }) {
                Canvas(modifier = Modifier.fillMaxHeight(), onDraw = {
                    drawLine(
                        color = Color.Black.copy(alpha = 0.5f),
                        start = Offset(0f, -30.dp.toPx()),
                        end = Offset(0f, if (isLastIndex) 0f else 30.dp.toPx()),
                        strokeWidth = 2f
                    )
                    drawLine(
                        color = Color.Black.copy(alpha = 0.5f),
                        start = Offset(0f, 0f),
                        end = Offset(30.dp.toPx(), 0f),
                        strokeWidth = 2f
                    )
                })
                Image(
                    painter = subCategoryEntity.icon.toPainterResource(),
                    contentDescription = subCategoryEntity.title,
                    modifier = Modifier
                        .scale(0.75f)
                        .offset(20.dp)
                )
                Text(
                    text = subCategoryEntity.name,
                    style = MaterialTheme.typography.body1,
                    color = Color.Black,
                    modifier = Modifier.offset(10.dp)
                )
                if (categorySelected.name == subCategoryEntity.name) {
                    Spacer(modifier = Modifier.weight(1f))
                    Image(
                        painter = painterResource(id = R.drawable.ic_check_green),
                        contentDescription = "Check"
                    )
                }
            }
        }
        if (!isLastIndexCategory) {
            Divider(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(10.dp)
                    .background(Gray.copy(alpha = 0.1f))
            )
        }
    }
}