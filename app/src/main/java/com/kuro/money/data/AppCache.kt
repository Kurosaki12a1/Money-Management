package com.kuro.money.data

import com.kuro.money.data.model.ConversionRates
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

object AppCache {
    private val _defaultCurrency = MutableStateFlow("VND")
    val defaultCurrency = _defaultCurrency.asStateFlow()

    private val _listRates = MutableStateFlow<Map<String, List<ConversionRates>>>(emptyMap())
    val listRates = _listRates.asStateFlow()


    fun updateDefaultCurrency(value : String) {
        _defaultCurrency.value = value
    }

    fun updateListRates(key: String, value : List<ConversionRates>){
        val updateMap = _listRates.value.toMutableMap().apply {
            put(key, value)
        }
        _listRates.value = updateMap.toMap()
    }
}