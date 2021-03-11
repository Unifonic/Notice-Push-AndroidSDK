package com.unifonic.noticesdk.base

import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.JsonParseException
import com.google.gson.stream.MalformedJsonException
import com.unifonic.noticesdk.AndroidApp
import com.unifonic.noticesdk.network.ApiErrorResponse
import com.unifonic.noticesdk.network.InvalidAuthException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException

open class BaseViewModel(val app:AndroidApp) : AndroidViewModel(app){


    fun launchApi(
        dispatcher: CoroutineDispatcher = Dispatchers.IO,
        block: suspend CoroutineScope.() -> Unit
    ) = viewModelScope.launch(dispatcher, block = block)

    fun onHandleError(error: Throwable): String {
        var message = "Unknown error"
        when (error) {
            is HttpException -> {
                val messageWithCode = ApiErrorResponse(error).message

                return messageWithCode

            }
            is SocketTimeoutException -> {
                message = "Failed to connect to server. Timeout"
                return message
            }
            is IOException -> {
                message = "Failed to access resource."
                return message
            }
            is InvalidAuthException -> {

                return "Invalid Credentials."
            }
            is JsonParseException -> {

                return "Error parsing data."
            }
            is MalformedJsonException -> {
                return "Error parsing data."
            }
        }
        return message
    }
}