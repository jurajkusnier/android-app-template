package com.jurajkusnier.androidapptemplate

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.LifecycleRegistry
import android.arch.lifecycle.MutableLiveData
import com.jurajkusnier.androidapptemplate.data.api.GitHubJobsApiService
import com.jurajkusnier.androidapptemplate.data.model.Job
import com.jurajkusnier.androidapptemplate.data.repository.GitHubJobsRepository
import com.jurajkusnier.androidapptemplate.ui.jobs.JobsViewModel
import io.reactivex.Observable
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.schedulers.Schedulers
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class JobsViewModelUnitTest {

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    lateinit var gitHubJobsApiService: GitHubJobsApiService
    lateinit var gitHubJobsRepository: GitHubJobsRepository
    lateinit var jobsViewModel: JobsViewModel

    val observerLambda : (List<Job>?) -> Unit = {}

    @Before
    fun doBefore() {
        gitHubJobsApiService = Mockito.mock(GitHubJobsApiService::class.java)
        gitHubJobsRepository = GitHubJobsRepository(gitHubJobsApiService)
        jobsViewModel = JobsViewModel(gitHubJobsRepository)
    }

    companion object {

        @BeforeClass @JvmStatic
        fun setupSchdulers() {
            RxAndroidPlugins.setInitMainThreadSchedulerHandler { _ -> Schedulers.io()}
        }
    }


    @Test
    fun emptyJobsListTest() {
        val result = emptyList<Job>()
        doTest(result)
    }

    private fun doTest(result:List<Job>) {
        Mockito.`when`(gitHubJobsApiService.searchPositions(anyString())).thenReturn(Observable.just(result))

        val observerMock = mock(observerLambda::class.java)
        val lifecycle = LifecycleRegistry(mock(LifecycleOwner::class.java))
        lifecycle.handleLifecycleEvent(Lifecycle.Event.ON_RESUME)

        jobsViewModel.searchPositions("")

        jobsViewModel.jobsResults.observe({lifecycle}, observerMock )

        verify(observerMock ).invoke(result)
    }

    @Test
    fun notEmptyJobsListTest() {
        val result = listOf(
                Job("ID_1","0","TITLE_1","LOCATION_1","TYPE","DESCRIPTION","","company",null,null,""),
                Job("ID_2","0","TITLE_2","LOCATION_2","TYPE","DESCRIPTION","","company",null,null,""),
                Job("ID_3","0","TITLE_3","LOCATION_3","TYPE","DESCRIPTION","","company",null,null,""),
                Job("ID_4","0","TITLE_4","LOCATION_4","TYPE","DESCRIPTION","","company",null,null,"")
        )
        doTest(result)
    }

    @Test
    fun liveDataTest() {
        val liveData = MutableLiveData<Int>()
        liveData.postValue(123)
        assertEquals(123, liveData.value)

        liveData.value = 321
        assertEquals(321, liveData.value)
    }
}
