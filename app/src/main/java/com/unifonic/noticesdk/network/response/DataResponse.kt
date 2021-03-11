package com.droidmech.attandanceutility.network.response

import com.google.gson.annotations.SerializedName

open class DataResponse<T> {
    @SerializedName("success")
    val success: Boolean = false
    @SerializedName("message")
    val message: String = ""
    @SerializedName("items")
    val items: List<T> = ArrayList()
    @SerializedName("items_total")
    val itemsTotal: Int = 0
    @SerializedName("item_count")
    val itemsCount: Int = 0
}