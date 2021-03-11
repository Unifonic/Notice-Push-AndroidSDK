package com.unifonic.noticesdk.models.requestmodels

import com.google.gson.annotations.SerializedName

data class RegisterDevice(@SerializedName("app_id") var appId: String, var identifier: String)