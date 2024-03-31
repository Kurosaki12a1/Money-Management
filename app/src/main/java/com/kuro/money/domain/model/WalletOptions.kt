package com.kuro.money.domain.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Archive
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.ui.graphics.vector.ImageVector


enum class WalletOptions(val value : String, val icon: ImageVector) {
    TRANSFER_MONEY("Transfer Money", Icons.Default.AttachMoney),
    EDIT("Edit", Icons.Default.Edit),
    ARCHIVE("Archive", Icons.Default.Archive),
    DELETE("Delete", Icons.Default.Delete)
}