package com.kuro.money.domain.model

import com.google.gson.annotations.SerializedName

data class Accounts(
    @SerializedName("name") val name: String?,
    @SerializedName("icon") val icon: String?,
    @SerializedName("uuid") val uuid: String?,
    @SerializedName("balance") val balance: Double?,
)