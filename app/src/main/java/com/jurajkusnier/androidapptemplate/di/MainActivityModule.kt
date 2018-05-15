package com.jurajkusnier.androidapptemplate.di

import android.arch.lifecycle.ViewModelProvider
import com.jurajkusnier.androidapptemplate.ui.MainActivity
import com.jurajkusnier.androidapptemplate.ui.jobs.JobsFragmentModule
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
internal abstract class MainActivityModule {
    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @ContributesAndroidInjector(modules = [JobsFragmentModule::class])
    internal abstract fun contributeMainActivity(): MainActivity
}