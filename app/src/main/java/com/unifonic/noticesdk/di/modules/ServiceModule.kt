package com.unifonic.noticesdk.di.modules


import android.app.Service
import dagger.Module
import dagger.Provides

@Module
class ServiceModule(var myService: Service){
    @Provides
    fun provideMyService():Service{
        return myService
    }
}
