package com.jurajkusnier.androidapptemplate.ui.jobs

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.jurajkusnier.androidapptemplate.di.ViewModelFactory
import com.jurajkusnier.androidapptemplate.di.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class JobsActivityModule {

    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(JobsViewModel::class)
    abstract fun bindMainViewModel(viewModel: JobsViewModel): ViewModel

}
