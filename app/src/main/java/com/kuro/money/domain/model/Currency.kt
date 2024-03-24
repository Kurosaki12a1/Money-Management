package com.kuro.money.domain.model

import com.google.gson.annotations.SerializedName

data class Currencies (
    @SerializedName("t") val time : Long?,
    @SerializedName("data") val data : List<Currency>
)

data class Currency(
    @SerializedName("c") val code: String?,
    @SerializedName("s") val symbol : String?,
    @SerializedName("n") val name : String?,
    @SerializedName("i") val id : Long?,
    @SerializedName("t") val type : Int?,
    @SerializedName("dm") val dm: String?,
    @SerializedName("gs") val gs: String?
)