package com.unifonic.noticesdk.di.modules


import com.unifonic.noticesdk.network.rest.UnifonicNotificationService
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Named
import javax.inject.Singleton

@Module
class DataModule {



    @Singleton
    @Provides
    fun getHomeService(@Named(RetrofitModule.PUBLIC_CLIENT) retrofit: Retrofit): UnifonicNotificationService {
        return retrofit.create(UnifonicNotificationService::class.java)
    }

}