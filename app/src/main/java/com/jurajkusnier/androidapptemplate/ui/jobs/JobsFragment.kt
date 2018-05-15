package com.jurajkusnier.androidapptemplate.ui.jobs

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.content.res.ResourcesCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.SearchView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jurajkusnier.androidapptemplate.R
import com.jurajkusnier.androidapptemplate.data.model.Job
import com.jurajkusnier.androidapptemplate.di.ViewModelFactory
import dagger.android.AndroidInjection
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.jobs_fragment.*
import javax.inject.Inject

/*
JobsFragment shows list of jobs from https://jobs.github.com/api
It uses retrofit library to load data from REST API.
All dependencies are injected by Dagger 2
 */
class JobsFragment: DaggerFragment() {

    val TAG = JobsFragment::class.simpleName

    lateinit var viewModel: JobsViewModel
    @Inject lateinit var viewModelFactory: ViewModelFactory

    var errorSnackbar:Snackbar? = null

    companion object {
        fun newInstance() = JobsFragment()
    }

    override fun onAttach(context: Context?) {
        //DI activity injection first
        AndroidInjection.inject(activity)
        super.onAttach(context)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.jobs_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        (activity as AppCompatActivity).setSupportActionBar(toolbar)

        val layoutManager = LinearLayoutManager(context)
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

            if (it == JobsViewModel.SearchState.ERROR) {
                errorSnackbar = Snackbar.make(recyclerViewJobs, getString(R.string.network_connection_error),Snackbar.LENGTH_INDEFINITE)
                errorSnackbar?.view?.setBackgroundColor(ResourcesCompat.getColor(resources, R.color.colorAccent,null))
                errorSnackbar?.show()
            } else {
                errorSnackbar?.dismiss()
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