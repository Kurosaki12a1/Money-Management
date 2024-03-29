package com.kuro.money.presenter.add_transaction.feature.event

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kuro.money.data.model.EventEntity
import com.kuro.money.data.utils.Resource
import com.kuro.money.domain.usecase.EventUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SelectEventViewModel @Inject constructor(
    private val eventUseCase: EventUseCase
) : ViewModel() {
    private val _getAllEvents = MutableStateFlow<Resource<List<EventEntity>?>>(Resource.Default)
    val getAllEvents = _getAllEvents.asStateFlow()

    init {
        getAllEvents()
    }

    fun getAllEvents() {
        viewModelScope.launch {
            eventUseCase().collectLatest {
                _getAllEvents.value = it
            }
        }
    }
}