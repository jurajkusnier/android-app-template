package com.jurajkusnier.androidapptemplate.ui.users

import dagger.Binds
import dagger.Module

@Module
abstract class UsersActivityModule {

    @Binds
    abstract fun provideUserActivity(activity: UsersActivity): UsersActivity

}
