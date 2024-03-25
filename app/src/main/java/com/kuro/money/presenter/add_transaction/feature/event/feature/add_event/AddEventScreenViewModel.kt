package com.kuro.money.presenter.add_transaction.feature.event.feature.add_event

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kuro.money.data.model.AccountEntity
import com.kuro.money.data.model.CurrencyEntity
import com.kuro.money.data.model.EventEntity
import com.kuro.money.data.utils.Resource
import com.kuro.money.domain.model.ScreenSelection
import com.kuro.money.domain.usecase.EventUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddEventScreenViewModel @Inject constructor(
    private val eventUseCase: EventUseCase
) : ViewModel() {

    private val _enableChildScreen = MutableStateFlow(ScreenSelection.ADD_EVENT_SCREEN)
    val enableChildScreen = _enableChildScreen.asStateFlow()

    private val _insertEvent = MutableStateFlow<Resource<Long>>(Resource.Default)
    val insertEvent = _insertEvent.asStateFlow()

    private val _iconSelected = MutableStateFlow<String?>(null)
    val iconSelected = _iconSelected.asStateFlow()

    private val _wallet = MutableStateFlow<AccountEntity?>(null)
    val wallet = _wallet.asStateFlow()

    private val _currencySelected = MutableStateFlow<CurrencyEntity?>(null)
    val currencySelected = _currencySelected.asStateFlow()

    fun insertEvent(event: EventEntity) {
        viewModelScope.launch {
            eventUseCase(event).collectLatest {
                _insertEvent.value = it
            }
        }
    }

    fun clearData() {
        _iconSelected.value = null
        _currencySelected.value = null
        _wallet.value = null
    }

    fun setCurrencySelected(value: CurrencyEntity) {
        _currencySelected.value = value
    }

    fun setIconSelected(value: String) {
        _iconSelected.value = value
    }

    fun setWallet(entity: AccountEntity) {
        _wallet.value = entity
    }

    fun setOpenWalletScreen(value: Boolean) {
        if (value) _enableChildScreen.value = ScreenSelection.WALLET_SCREEN
        else _enableChildScreen.value = ScreenSelection.ADD_EVENT_SCREEN
    }

    fun setOpenSelectIconScreen(value: Boolean) {
        if (value) {
            _enableChildScreen.value = ScreenSelection.SELECT_ICON_SCREEN
        } else _enableChildScreen.value = ScreenSelection.ADD_EVENT_SCREEN
    }

    fun setOpenCurrencyScreen(value: Boolean) {
        if (value) {
            _enableChildScreen.value = ScreenSelection.SELECT_CURRENCY_SCREEN
        } else _enableChildScreen.value = ScreenSelection.ADD_EVENT_SCREEN
    }
}