package com.kuro.money.domain.model

import com.google.gson.annotations.SerializedName

data class Category (
    @SerializedName("name") val name: String?,
    @SerializedName("title") val title: String?,
    @SerializedName("icon") val icon: String?,
    @SerializedName("type") val type: String?,
    @SerializedName("metadata") val metadata: String?,
    @SerializedName("subcategories") val subCategories : List<SubCategory>?
)

data class SubCategory(
    @SerializedName("name") val name: String?,
    @SerializedName("title") val title: String?,
    @SerializedName("icon") val icon: String?,
    @SerializedName("type") val type: String?,
    @SerializedName("metadata") val metadata: String?,
)

data class SelectedCategory(
    val name : String,
    val icon : String
)