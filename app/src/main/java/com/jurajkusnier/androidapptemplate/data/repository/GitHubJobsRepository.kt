package com.jurajkusnier.androidapptemplate.data.repository

import com.jurajkusnier.androidapptemplate.data.api.GitHubJobsApiService
import com.jurajkusnier.androidapptemplate.data.model.Job
import io.reactivex.Observable
import javax.inject.Inject

class GitHubJobsRepository @Inject constructor(private val apiService:GitHubJobsApiService ) {

    fun searchPosition(search:String): Observable<List<Job>> {
        return apiService.searchPositions(search)
    }
}