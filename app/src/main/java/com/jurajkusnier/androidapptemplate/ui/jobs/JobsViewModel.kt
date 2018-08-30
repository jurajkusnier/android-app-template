package com.jurajkusnier.androidapptemplate.ui.jobs

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.util.Log
import com.jurajkusnier.androidapptemplate.data.api.OfflineException
import com.jurajkusnier.androidapptemplate.data.model.Job
import com.jurajkusnier.androidapptemplate.data.repository.GitHubJobsRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject


class JobsViewModel @Inject constructor(private val repository: GitHubJobsRepository): ViewModel() {

    enum class SearchState {DONE, IN_SEARCH, ERROR, ERROR_OFFLINE}

    private val TAG = JobsViewModel::class.simpleName

    private var disposable: Disposable? = null

    var lastQuery:String? = null
        private set

    private val _searchState: MutableLiveData<SearchState> = MutableLiveData()
    val searchState: LiveData<SearchState>
        get() = _searchState

    private val _jobsResults: MutableLiveData<List<Job>> = MutableLiveData()
    val jobsResults: LiveData<List<Job>>
        get() = _jobsResults


    init {
        _searchState.value = SearchState.DONE
    }

    override fun onCleared() {
        disposable?.dispose()
        super.onCleared()
    }

    fun searchPositions(search:String?) {
        lastQuery = search

        if (search == null) {
            _jobsResults.value = listOf()
            return
        }

        disposable?.dispose()

        _searchState.value = SearchState.IN_SEARCH

        disposable = repository.searchPosition(search)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        {
                            data ->
                                _jobsResults.value = data
                                _searchState.value = SearchState.DONE
                        },
                        {
                            error ->
                                if (error is OfflineException) {
                                    _searchState.value = SearchState.ERROR_OFFLINE
                                } else {
                                    _searchState.value = SearchState.ERROR
                                }
                                Log.e(TAG,Log.getStackTraceString(error))
                        }
                )
    }

}