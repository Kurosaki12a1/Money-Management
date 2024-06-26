package com.kuro.money.presenter.add_transaction.feature.event.feature.add_event

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kuro.money.data.model.AccountEntity
import com.kuro.money.data.model.Currency
import com.kuro.money.data.model.Event
import com.kuro.money.data.utils.Resource
import com.kuro.money.domain.usecase.EventUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class AddEventScreenViewModel @Inject constructor(
    private val eventUseCase: EventUseCase
) : ViewModel() {

    private val _insertEvent = MutableStateFlow<Resource<Long>>(Resource.Default)
    val insertEvent = _insertEvent.asStateFlow()

    private val _iconSelected = MutableStateFlow<String?>(null)
    val iconSelected = _iconSelected.asStateFlow()

    private val _wallet = MutableStateFlow<AccountEntity?>(null)
    val wallet = _wallet.asStateFlow()

    private val _name = MutableStateFlow("")
    val name = _name.asStateFlow()

    private val _endingDate = MutableStateFlow<LocalDate>(LocalDate.now())
    val endingDate = _endingDate.asStateFlow()

    private val _currencySelected = MutableStateFlow<Currency?>(null)
    val currencySelected = _currencySelected.asStateFlow()

    fun insertEvent() {
        if (name.value.isEmpty() || wallet.value == null || iconSelected.value == null || currencySelected.value == null) return
        val event = Event(
            0L,
            iconSelected.value!!,
            name.value,
            startDate = LocalDate.now(),
            endDate = endingDate.value,
            currencyId = currencySelected.value!!.id,
            walletId = _wallet.value!!.id
        )
        viewModelScope.launch {
            eventUseCase(event).collectLatest {
                _insertEvent.value = it
            }
        }
    }

    fun setName(name: String) {
        _name.value = name
    }

    fun setEndingDate(date: LocalDate) {
        _endingDate.value = date
    }

    fun setCurrencySelected(value: Currency) {
        _currencySelected.value = value
    }

    fun setIconSelected(value: String) {
        _iconSelected.value = value
    }

    fun setWallet(entity: AccountEntity) {
        _wallet.value = entity
    }

}