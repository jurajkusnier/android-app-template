package com.jurajkusnier.androidapptemplate.di

import com.jurajkusnier.androidapptemplate.ui.jobs.JobsActivityModule
import com.jurajkusnier.androidapptemplate.ui.jobs.JobsActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBuilder {
    @ContributesAndroidInjector(modules = [JobsActivityModule::class])
    internal abstract fun bindJobsActivity(): JobsActivity
}