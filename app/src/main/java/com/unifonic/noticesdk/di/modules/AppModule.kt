package com.unifonic.noticesdk.di.modules

import android.content.Context
import com.unifonic.noticesdk.AndroidApp
import com.unifonic.noticesdk.di.AppContext

import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule(val app: AndroidApp) {

    @Provides
    @Singleton
    fun provideApp() = app



    @Provides
    @AppContext
    internal fun provideContext(): Context {
        return app.applicationContext
    }
}