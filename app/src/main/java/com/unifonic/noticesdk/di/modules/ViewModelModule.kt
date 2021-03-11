package com.unifonic.noticesdk.di.modules

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.unifonic.noticesdk.di.ViewModelFactory
import com.unifonic.noticesdk.di.ViewModelKey
import com.unifonic.noticesdk.ui.DashBoardViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {

    @Binds
    internal abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    //Add more ViewModels here

    @Binds
    @IntoMap
    @ViewModelKey(DashBoardViewModel::class)
    internal abstract fun dashBoardViewModel(viewModel: DashBoardViewModel): ViewModel
}