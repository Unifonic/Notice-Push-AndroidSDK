package com.unifonic.noticesdk.di.components


import com.unifonic.noticesdk.ui.MainActivity
import com.unifonic.noticesdk.di.ViewScope
import com.unifonic.noticesdk.di.modules.ViewModule
import dagger.Subcomponent


@ViewScope
@Subcomponent(modules = [(ViewModule::class)])
interface ViewComponent {

    fun inject(mainActivity: MainActivity)
}
