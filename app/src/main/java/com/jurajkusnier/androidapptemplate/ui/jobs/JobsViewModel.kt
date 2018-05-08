package com.jurajkusnier.androidapptemplate.ui.jobs

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.util.Log
import com.jurajkusnier.androidapptemplate.data.model.Job
import com.jurajkusnier.androidapptemplate.data.repository.GitHubJobsRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject


class JobsViewModel @Inject constructor(val repository: GitHubJobsRepository): ViewModel() {

    enum class SearchState {DONE, IN_SEARCH, ERROR}

    private val TAG = JobsViewModel::class.simpleName

    var lastQuery:String? = null
        private set
    var searchState: MutableLiveData<SearchState> = MutableLiveData()
        private set
    var jobsResults: MutableLiveData<List<Job>> = MutableLiveData()
        private set

    private var disposable: Disposable? = null

    init {
        searchState.value = SearchState.DONE
    }

    override fun onCleared() {
        disposable?.dispose()
        super.onCleared()
    }

    fun searchPositions(search:String?) {
        lastQuery = search

        if (search == null) {
            jobsResults.value = listOf()
            return
        }

        disposable?.dispose()

        searchState.value = SearchState.IN_SEARCH

        disposable = repository.searchPosition(search)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        {
                            data ->
                                jobsResults.value = data
                                searchState.value = SearchState.DONE
                        },
                        {
                            error ->
                                Log.e(TAG,Log.getStackTraceString(error))
                                searchState.value = SearchState.ERROR
                             }
                )
    }

}