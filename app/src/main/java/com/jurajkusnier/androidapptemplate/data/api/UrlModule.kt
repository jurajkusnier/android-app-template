package com.jurajkusnier.androidapptemplate.data.api

import dagger.Module
import dagger.Provides
import javax.inject.Named
import javax.inject.Singleton

@Module
class UrlModule {

    @Provides
    @Singleton
    @Named("baseUrl")
    fun provideBaseUrl(): String {
        return "https://jobs.github.com/"
    }
}