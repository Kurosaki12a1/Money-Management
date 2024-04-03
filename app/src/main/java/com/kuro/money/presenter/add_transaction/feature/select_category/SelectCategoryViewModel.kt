package com.kuro.money.presenter.add_transaction.feature.select_category

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.kuro.money.data.model.CategoryEntity
import com.kuro.money.data.model.SubCategoryEntity
import com.kuro.money.data.utils.Resource
import com.kuro.money.domain.model.SelectedCategory
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

    private val _getSubCategoryResponse =
        MutableStateFlow<Resource<List<SubCategoryEntity>>>(Resource.Default)
    val getSubCategoryResponse = _getSubCategoryResponse.asStateFlow()

    private val _selectedCategory = MutableStateFlow(SelectedCategory())
    val selectedCategory = _selectedCategory.asStateFlow()

    init {
        getCategories()
        getSubCategories()
    }

    private fun getCategories() {
        viewModelScope.launch {
            categoryUseCase.getAllCategories().collectLatest {
                _getCategoryResponse.value = it
            }
        }
    }

    private fun getSubCategories() {
        viewModelScope.launch {
            categoryUseCase.getAllSubCategories().collectLatest {
                _getSubCategoryResponse.value = it
            }
        }
    }

    fun setSelectedCategories(selectedCategory: SelectedCategory) {
        _selectedCategory.value = selectedCategory
    }
}