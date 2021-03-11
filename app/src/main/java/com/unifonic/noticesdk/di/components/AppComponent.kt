package com.unifonic.noticesdk.di.components


import com.unifonic.noticesdk.AndroidApp
import com.unifonic.noticesdk.di.components.ServiceComponent
import com.unifonic.noticesdk.di.components.ViewComponent
import com.unifonic.noticesdk.di.modules.*
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, RetrofitModule::class, DataModule::class, ViewModelModule::class, PlayerModule::class])

interface AppComponent {

    fun inject(app: AndroidApp)

    fun plus(viewModule: ViewModule): ViewComponent

    fun plus(serviceModule: ServiceModule): ServiceComponent

}