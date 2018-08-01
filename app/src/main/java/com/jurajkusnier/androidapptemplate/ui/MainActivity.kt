package com.jurajkusnier.androidapptemplate.ui

import android.os.Bundle
import com.jurajkusnier.androidapptemplate.R
import com.jurajkusnier.androidapptemplate.ui.jobs.JobsFragment
import dagger.android.AndroidInjection
import dagger.android.support.DaggerAppCompatActivity

class MainActivity: DaggerAppCompatActivity() {

    val TAG = MainActivity::class.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.container, JobsFragment.newInstance())
                    .commitNow()
        }
    }

}