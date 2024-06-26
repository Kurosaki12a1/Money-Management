package com.kuro.money.presenter.add_transaction.feature.select_category

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kuro.money.data.model.CategoryEntity
import com.kuro.money.data.utils.Resource
import com.kuro.money.domain.usecase.CategoryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SelectCategoryViewModel @Inject constructor(
    private val categoryUseCase: CategoryUseCase
) : ViewModel() {
    private val _getCategoryResponse =
        MutableStateFlow<Resource<List<CategoryEntity>>>(Resource.Default)
    val getCategoryResponse = _getCategoryResponse.asStateFlow()

    private val _selectedCategory = MutableStateFlow<CategoryEntity?>(null)
    val selectedCategory = _selectedCategory.asStateFlow()

    init {
        getCategories()
    }

    private fun getCategories() {
        viewModelScope.launch {
            categoryUseCase.getAllCategories().collectLatest {
                _getCategoryResponse.value = it
            }
        }
    }

    fun setSelectedCategories(selectedCategory: CategoryEntity?) {
        _selectedCategory.value = selectedCategory
    }
}