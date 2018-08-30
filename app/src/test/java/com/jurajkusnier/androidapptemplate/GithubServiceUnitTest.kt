package com.jurajkusnier.androidapptemplate

import android.arch.core.executor.testing.InstantTaskExecutorRule
import com.jurajkusnier.androidapptemplate.data.api.GitHubJobsApiService
import com.jurajkusnier.androidapptemplate.data.api.NetworkModule
import com.jurajkusnier.androidapptemplate.data.model.Job
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

class GithubServiceUnitTest {

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    lateinit var gitHubJobsApiService: GitHubJobsApiService
    private val server = MockWebServer()

    @Before
    fun doSetup() {
        val networkModule = NetworkModule()
        server.start()

        val serverUrl = server.url("/").toString()
        val moshi = networkModule.provideMoshi()
        val httpClient = networkModule.provideHttpClient()
        val retrofit = networkModule.provideRetrofit(serverUrl, moshi, httpClient)

        gitHubJobsApiService = networkModule.provideGitHubJobsApiService(retrofit)
    }

    @After
    fun doCleanup() {
        server.shutdown()
    }

    @Test
    fun serverErrorTest() {
        server.enqueue(MockResponse().setResponseCode(500).setBody("Server Error"))
        gitHubJobsApiService.searchPositions("test")
                .test()
                .assertNoValues()
    }


    @Test
    fun searchPositionsTest() {
        server.enqueue(MockResponse().setResponseCode(200).setBody(
                """
                    [{
    "id": "ID",
    "created_at": "today",
    "title": "Title",
    "location": "Location",
    "type": "Full Time",
    "description": "...",
    "how_to_apply": "...",
    "company": "Verusoft",
    "company_url": null,
    "company_logo": null,
    "url": "http://localhost/"
},
    {
    "id": "ID_2",
    "created_at": "today",
    "title": "Title",
    "location": "Location",
    "type": "Full Time",
    "description": "...",
    "how_to_apply": "...",
    "company": "Verusoft",
    "company_url": null,
    "company_logo": null,
    "url": "http://localhost/"
}]
                """.trimIndent()

        ))

        gitHubJobsApiService.searchPositions("test")
                .test()
                .assertResult(
                        listOf(
                                Job("ID","today","Title","Location","Full Time","...","...","Verusoft",null,null,"http://localhost/"),
                                Job("ID_2","today","Title","Location","Full Time","...","...","Verusoft",null,null,"http://localhost/")
                        )

                )

    }
}