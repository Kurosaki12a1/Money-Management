package com.kuro.money.domain.model

import com.kuro.money.constants.Constants
import com.kuro.money.presenter.utils.DecimalFormatter
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
    data class Over(val value: Double = 0.0) : AdvancedSearchAmount()

    data class Under(val value: Double = 0.0) : AdvancedSearchAmount()

    data class Between(val value1: Double = 0.0, val value2: Double = 0.0) : AdvancedSearchAmount()

    data class Exact(val value: Double = 0.0) : AdvancedSearchAmount()

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

    fun string(decimalFormatter: DecimalFormatter = DecimalFormatter()): String {
        return when (this) {
            is All -> "All"
            is Over -> "Over ${decimalFormatter.formatForVisual(value.string())}"
            is Under -> "Under ${decimalFormatter.formatForVisual(value.string())}"
            is Between -> "Between ${decimalFormatter.formatForVisual(value1.string())} - ${
                decimalFormatter.formatForVisual(value2.string())}"
            is Exact -> "Exact ${decimalFormatter.formatForVisual(value.string())}"
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

fun buildTimeCondition(time: AdvancedSearchTime): String {
    return when (time) {
        is AdvancedSearchTime.All -> ""
        is AdvancedSearchTime.After -> "  t.displayDate >= '${time.time}'"
        is AdvancedSearchTime.Before -> "  t.displayDate < '${time.time}'"
        is AdvancedSearchTime.Between -> "  t.displayDate BETWEEN '${time.from}' AND '${time.to}'"
        is AdvancedSearchTime.Exact -> "  t.displayDate = '${time.time}'"
        else -> ""
    }
}

fun buildAmountCondition(amount: AdvancedSearchAmount): String {
    return when (amount) {
        is AdvancedSearchAmount.All -> ""
        is AdvancedSearchAmount.Exact -> " t.amount = ${amount.value}"
        is AdvancedSearchAmount.Under -> " t.amount < ${amount.value}"
        is AdvancedSearchAmount.Over -> " t.amount > ${amount.value}"
        is AdvancedSearchAmount.Between -> " t.amount BETWEEN ${amount.value1} AND ${amount.value2} "
        else -> ""
    }
}

fun buildCategoryCondition(category: AdvancedSearchCategory): String {
    return when (category) {
        AdvancedSearchCategory.ALL_CATEGORIES -> ""
        AdvancedSearchCategory.ALL_INCOME -> " c.type = 'income'"
        else -> " c.type = 'expense'"
    }
}

fun buildWalletCondition(wallet: String): String {
    return if (wallet == Constants.GLOBAL_WALLET_NAME || wallet == Constants.ALL_WALLETS) {
        ""
    } else
        " w.name = '$wallet' "
}

fun buildWithCondition(with: String): String {
    return if (with.isBlank()) "" else " t.people LIKE '$with%  "
}

fun buildNoteCondition(note: String): String {
    return if (note.isBlank()) "" else " t.note LIKE '$note%  "
}

