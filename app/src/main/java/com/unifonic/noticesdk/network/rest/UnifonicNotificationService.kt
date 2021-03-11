package com.unifonic.noticesdk.network.rest


import com.unifonic.noticesdk.models.requestmodels.Channel
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface UnifonicNotificationService {

    //Suppose to return device id
    // in 'identifier' key
    @Multipart
    @POST("apps/register")
    suspend fun register(@Part("app_id") appId: RequestBody,@Part("identifier") identifier: RequestBody,): HashMap<String, String>

    @Multipart
    @POST("bindings/update_status")
    suspend fun disableNotification(@Part("address") address: RequestBody, @Part("status") status: RequestBody,): Any

    @Multipart
    @POST("bindings")
    suspend fun saveToken(
        @Part("address") address: RequestBody,
        @Part("identifier") identifier: RequestBody,
        @Part("type") type: RequestBody,
    ): Any

    @Multipart
    @POST("bindings/refresh")
    suspend fun updateToken(
        @Part("old_address") oldAddress: RequestBody,
        @Part("address") newAddress: RequestBody
    ): Any

    @POST("notifications/read")
    @Multipart
    suspend fun markNotificationRead(@Part("message_id") messageId: RequestBody): Any

    @POST("notifications/received")
    @Multipart
    suspend fun markNotificationReceived(@Part("message_id") messageId: RequestBody): Any
}