package com.kuro.money.domain.model

import com.google.gson.annotations.SerializedName

data class Category(
    @SerializedName("id") val id: Long?,
    @SerializedName("name") val name: String?,
    @SerializedName("title") val title: String?,
    @SerializedName("icon") val icon: String?,
    @SerializedName("type") val type: String?,
    @SerializedName("metadata") val metadata: String?,
    @SerializedName("subcategories") val subCategories: List<SubCategory>?
)

data class SubCategory(
    @SerializedName("id") val id: Long?,
    @SerializedName("parentId") val parentId: Long?,
    @SerializedName("name") val name: String?,
    @SerializedName("title") val title: String?,
    @SerializedName("icon") val icon: String?,
    @SerializedName("type") val type: String?,
    @SerializedName("metadata") val metadata: String?,
)

data class SelectedCategory(
    val name: String = "",
    val icon: String = "",
    val type: String = ""
)