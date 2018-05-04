package com.jurajkusnier.androidapptemplate.di

import com.jurajkusnier.androidapptemplate.ui.users.UsersActivityModule
import com.jurajkusnier.androidapptemplate.ui.users.UsersActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBuilder {
    @ContributesAndroidInjector(modules = [UsersActivityModule::class])
    internal abstract fun bindUsersActivity(): UsersActivity
}