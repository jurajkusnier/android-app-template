package com.jurajkusnier.androidapptemplate.ui.users

import android.os.Bundle
import com.jurajkusnier.androidapptemplate.R
import dagger.android.support.DaggerAppCompatActivity

/*
UserActivity shows list of users from https://jsonplaceholder.typicode.com/users
It uses retrofit library to load data from REST API.
All dependencies are injected by Dagger 2
 */
class UsersActivity: DaggerAppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_users)
    }


}