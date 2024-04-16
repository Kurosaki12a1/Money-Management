package com.kuro.money.domain.model

import com.kuro.money.constants.Constants
import com.kuro.money.presenter.utils.string
import java.time.LocalDate

enum class AdvancedSearchCategory(val value: String) {
    ALL_CATEGORIES(Constants.ALL_CATEGORIES),
    ALL_INCOME(Constants.ALL_INCOME),
    ALL_EXPENSE(Constants.ALL_EXPENSE);

    companion object {
        fun getString(index: Int): String {
            if (index < 0 || index > entries.size) return ""
            return entries[index].value
        }
    }
}

sealed class AdvancedSearchAmount {
    data object All : AdvancedSearchAmount()
    data class Over(val value: String = "") : AdvancedSearchAmount()

    data class Under(val value: String = "") : AdvancedSearchAmount()

    data class Between(val value1: String = "", val value2: String = "") : AdvancedSearchAmount()

    data class Exact(val value: String = "") : AdvancedSearchAmount()

    // Enable Search
    data object Enabled : AdvancedSearchAmount()

    // Disable Search
    data object Disabled : AdvancedSearchAmount()

    fun name(): String {
        return when (this) {
            is All -> "All"
            is Over -> "Over"
            is Under -> "Under"
            is Between -> "Between"
            is Exact -> "Exact"
            else -> ""
        }
    }

    fun string(): String {
        return when (this) {
            is All -> "All"
            is Over -> "Over $value"
            is Under -> "Under $value"
            is Between -> "Between $value1 - $value2"
            is Exact -> "Exact $value"
            else -> ""
        }
    }

}

sealed class AdvancedSearchTime {
    data object All : AdvancedSearchTime()

    data class After(val time: LocalDate = LocalDate.now()) : AdvancedSearchTime()

    data class Before(val time: LocalDate = LocalDate.now()) : AdvancedSearchTime()

    data class Between(val from: LocalDate = LocalDate.now(), val to: LocalDate = LocalDate.now()) :
        AdvancedSearchTime()

    data class Exact(val time: LocalDate = LocalDate.now()) : AdvancedSearchTime()

    // Enable Search
    data object Enabled : AdvancedSearchTime()

    // Disable Search
    data object Disabled : AdvancedSearchTime()

    fun name(): String {
        return when (this) {
            is All -> "All"
            is After -> "After"
            is Before -> "Before"
            is Between -> "Between"
            is Exact -> "Exact"
            else -> ""
        }
    }

    fun string(): String {
        return when (this) {
            is All -> "All"
            is After -> "After ${time.string(enableToday = false)}"
            is Before -> "Before ${time.string(enableToday = false)}"
            is Between -> "Between ${from.string(enableToday = false)} - ${to.string(enableToday = false)}"
            is Exact -> "Exact ${time.string(enableToday = false)}"
            else -> ""
        }
    }
}