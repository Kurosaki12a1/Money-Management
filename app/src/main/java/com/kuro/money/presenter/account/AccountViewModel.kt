package com.kuro.money.presenter.account

import androidx.lifecycle.ViewModel
import com.kuro.money.domain.usecase.AccountsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AccountViewModel @Inject constructor(
    private val walletUseCase: AccountsUseCase
) : ViewModel() {

}