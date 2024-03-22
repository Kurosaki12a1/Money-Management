package com.kuro.money.presenter.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kuro.money.data.utils.Resource
import com.kuro.money.domain.usecase.CategoryUseCase
import com.kuro.money.domain.usecase.PreferencesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class MainViewModel @Inject constructor(
    private val categoryUseCase: CategoryUseCase, private val preferencesUseCase: PreferencesUseCase
) : ViewModel() {
    private val _shouldOpenAddTransactionScreen = MutableStateFlow(false)
    val shouldOpenAddTransactionScreen = _shouldOpenAddTransactionScreen.asStateFlow()

    init {
        getAndInsertCategoriesFromJson()
    }

    fun setOpenAddTransactionScreen(value: Boolean) {
        _shouldOpenAddTransactionScreen.value = value
    }

    private fun getAndInsertCategoriesFromJson() {
        viewModelScope.launch {
            preferencesUseCase.isFirstTimeOpenApp().first().let { isFirstTimeOpen ->
                if (isFirstTimeOpen) {
                    categoryUseCase("categories.json").flatMapLatest {
                        when (it) {
                            is Resource.Success -> {
                                if (it.value.isNullOrEmpty()) flowOf(Resource.failure(Exception("No data from Json")))
                                else categoryUseCase(it.value)
                            }
                            else -> flowOf(it) as Flow<Resource<Long>>
                        }
                    }.collectLatest {
                        preferencesUseCase.setFirstTimeOpenApp(false).collectLatest {  }
                    }
                }
            }
        }
    }

}