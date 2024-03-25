package com.kuro.money.domain.model

import com.google.gson.annotations.SerializedName

data class IconPack(
    @SerializedName("name") val name: String?,
    @SerializedName("product_id") val productId : String?,
    @SerializedName("icons") val icons: Map<String,List<String>>?,
    @SerializedName("tags") val tags: List<String>?
)