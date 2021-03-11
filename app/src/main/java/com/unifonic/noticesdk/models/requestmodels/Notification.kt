package com.unifonic.noticesdk.models.requestmodels

import com.google.gson.annotations.SerializedName

data class Notification(@SerializedName("message_id") var messageId:String)