package com.unifonic.noticesdk.di.modules


import com.unifonic.noticesdk.AndroidApp
import com.unifonic.noticesdk.BuildConfig
import com.unifonic.noticesdk.extensions.getAppId
import com.unifonic.noticesdk.extensions.getIdentifier
import com.unifonic.noticesdk.extensions.getSdkToken
import dagger.Module
import dagger.Provides
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton


@Module
class RetrofitModule {

    companion object {
        const val PUBLIC_CLIENT = "publicClient"
        const val AUTH_CLIENT = "authClient"
        const val WEB_CLIENT = "webClient"
    }

    @Singleton
    @Provides
    @Named(PUBLIC_CLIENT)
    fun getPublicRetrofitClient(): Retrofit {

        val client = OkHttpClient.Builder()
//        if (BuildConfig.enabledLogging) {

        client.addInterceptor { chain: Interceptor.Chain? ->

            val request: Request? = chain?.request()
                ?.newBuilder().apply {
                    this?.addHeader("Accept-Language", Locale.getDefault().language)
                    AndroidApp.instance.getSdkToken()?.let{
                        this?.addHeader("Authorization", "Bearer ${it}")
                    }
                    AndroidApp.instance.getAppId()?.let {
                        this?.addHeader("x-notice-app-id", it)
                    }
                }
                ?.build()

            chain?.proceed(request!!)!!
        }


        val logging = HttpLoggingInterceptor()

            logging.level = HttpLoggingInterceptor.Level.BODY
            client.addInterceptor(logging)
//        }
        client.connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)

        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client.build())
            .build()
    }
}