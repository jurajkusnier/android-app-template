package com.jurajkusnier.androidapptemplate

import com.jurajkusnier.androidapptemplate.di.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication

class App: DaggerApplication() {

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        val builder = DaggerAppComponent.builder().application(this).build()
        builder.inject(this)
        return builder
    }
}