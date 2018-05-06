package com.jurajkusnier.androidapptemplate.di

import android.app.Application
import android.content.Context
import com.jurajkusnier.androidapptemplate.data.api.NetworkModule
import dagger.Binds
import dagger.Module

@Module(includes = [NetworkModule::class])
abstract class AppModule {

    @Binds
    abstract fun provideContext(application: Application): Context
}