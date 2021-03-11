package com.unifonic.noticesdk.di.components


import android.app.Service
import com.unifonic.noticesdk.FirebaseMessagingService
import com.unifonic.noticesdk.di.ViewScope
import com.unifonic.noticesdk.di.modules.ServiceModule
import dagger.Subcomponent

@ViewScope
@Subcomponent(modules = [ServiceModule::class])
interface ServiceComponent {
    fun inject(service: FirebaseMessagingService)
}
