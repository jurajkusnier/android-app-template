package com.jurajkusnier.androidapptemplate.ui.jobs

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.SearchView
import android.view.View
import com.jurajkusnier.androidapptemplate.R
import com.jurajkusnier.androidapptemplate.data.model.Job
import com.jurajkusnier.androidapptemplate.di.ViewModelFactory
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_users.*
import javax.inject.Inject


/*
UserActivity shows list of jobs from https://jobs.github.com/api
It uses retrofit library to load data from REST API.
All dependencies are injected by Dagger 2
 */
class JobsActivity: DaggerAppCompatActivity() {

    val TAG = JobsActivity::class.simpleName

    lateinit var viewModel: JobsViewModel
    @Inject lateinit var viewModelFactory: ViewModelFactory


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_users)
        setSupportActionBar(toolbar)

        val layoutManager = LinearLayoutManager(this)
        recyclerViewJobs.layoutManager = layoutManager

        val dividerItemDecoration = DividerItemDecoration(recyclerViewJobs.context, layoutManager.orientation)
        recyclerViewJobs.addItemDecoration(dividerItemDecoration)

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(JobsViewModel::class.java)

        viewModel.jobsResults.observe(this, Observer<List<Job>> {
            t ->  recyclerViewJobs.adapter = JobsViewAdapter(t)
            if (t?.isNotEmpty() == true) {
                noResultsLayout.visibility = View.GONE
            } else {
                noResultsLayout.visibility = View.VISIBLE
            }
        })

        if (!viewModel.lastQuery.isNullOrEmpty()) {
            searchViewJobs.setQuery(viewModel.lastQuery,false)
        }

        viewModel.searchState.observe(this,Observer<JobsViewModel.SearchState> {

            if (it == JobsViewModel.SearchState.IN_SEARCH) {
                    progressBar.visibility = View.VISIBLE
                    progressBackdrop.visibility = View.VISIBLE
                } else {
                    progressBar.visibility = View.GONE
                    progressBackdrop.visibility = View.GONE
            }
        })

        searchViewJobs.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                viewModel.searchPositions(query)
                searchViewJobs.clearFocus()
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })

    }
}