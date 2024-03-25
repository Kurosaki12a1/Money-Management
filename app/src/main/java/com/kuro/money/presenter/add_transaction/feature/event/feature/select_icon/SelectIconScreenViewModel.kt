package com.kuro.money.presenter.add_transaction.feature.event.feature.select_icon

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kuro.money.domain.usecase.GetIconUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SelectIconScreenViewModel @Inject constructor(
    private val getIconUseCase: GetIconUseCase
) : ViewModel() {

    private val _getListIcon = MutableStateFlow<List<String>>(listOf())
    val getListIcon = _getListIcon.asStateFlow()

    init {
        getListIcon()
    }

    private fun getListIcon() {
        viewModelScope.launch {
            getIconUseCase().collectLatest {
                _getListIcon.value = it
            }
        }
    }
}