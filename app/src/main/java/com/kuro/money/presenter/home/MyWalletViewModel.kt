package com.kuro.money.presenter.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kuro.money.data.model.AccountEntity
import com.kuro.money.data.utils.Resource
import com.kuro.money.domain.usecase.AccountsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyWalletViewModel @Inject constructor(
    private val accountsUseCase: AccountsUseCase
) : ViewModel(){

    private val _allWallets = MutableStateFlow<Resource<List<AccountEntity>>>(Resource.Default)
    val allWallets = _allWallets.asStateFlow()

    fun getAllWallets() {
        viewModelScope.launch {
            accountsUseCase().collectLatest {
                _allWallets.value = it
            }
        }
    }
}