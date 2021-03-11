package com.unifonic.noticesdk.models

import com.google.gson.annotations.SerializedName

enum class NotificationReadType {
    @SerializedName("read")
    READ,
    @SerializedName("received")
    RECEIVED
}