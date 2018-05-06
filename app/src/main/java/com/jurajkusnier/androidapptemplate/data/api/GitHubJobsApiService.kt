package com.jurajkusnier.androidapptemplate.data.api

import com.jurajkusnier.androidapptemplate.data.model.Job
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query


interface GitHubJobsApiService {

    @GET("positions.json?")
    fun searchPositions(@Query("search") search:String): Observable<List<Job>>

}