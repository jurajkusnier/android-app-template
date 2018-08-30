package com.jurajkusnier.androidapptemplate

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Observer
import com.jurajkusnier.androidapptemplate.data.api.GitHubJobsApiService
import com.jurajkusnier.androidapptemplate.data.model.Job
import com.jurajkusnier.androidapptemplate.data.repository.GitHubJobsRepository
import com.jurajkusnier.androidapptemplate.ui.jobs.JobsViewModel
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.internal.schedulers.ExecutorScheduler
import io.reactivex.plugins.RxJavaPlugins
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mockito
import org.mockito.Mockito.verify
import java.util.concurrent.Executor

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class JobsViewModelUnitTest {

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    private lateinit var gitHubJobsApiService: GitHubJobsApiService
    private lateinit var gitHubJobsRepository: GitHubJobsRepository
    private lateinit var jobsViewModel: JobsViewModel

    @Before
    fun doBefore() {
        gitHubJobsApiService = Mockito.mock(GitHubJobsApiService::class.java)
        gitHubJobsRepository = Mockito.mock(GitHubJobsRepository::class.java)
        jobsViewModel = JobsViewModel(gitHubJobsRepository)
    }

    companion object {
        private val immediate: Scheduler = object : Scheduler() {
            override fun createWorker(): Scheduler.Worker {
                return ExecutorScheduler.ExecutorWorker(Executor { it.run() })
            }
        }

        @BeforeClass @JvmStatic
        fun setupSchedulers() {
            RxJavaPlugins.setInitIoSchedulerHandler{ _ -> immediate }
            RxAndroidPlugins.setInitMainThreadSchedulerHandler{ _ -> immediate}
        }
    }

    @Test
    fun nullSearchQuery() {
        val observer = mock<Observer<List<Job>>>()
        jobsViewModel.jobsResults.observeForever(observer)

        Mockito.verifyNoMoreInteractions(observer)
        Mockito.verifyNoMoreInteractions(gitHubJobsRepository)

        jobsViewModel.searchPositions(null)
        verify(observer).onChanged(listOf())
    }

    @Test
    fun jobsResults() {
        val result = listOf(
                Job("ID_1","0","TITLE_1","LOCATION_1","TYPE","DESCRIPTION","","company",null,null,""),
                Job("ID_2","0","TITLE_2","LOCATION_2","TYPE","DESCRIPTION","","company",null,null,""),
                Job("ID_3","0","TITLE_3","LOCATION_3","TYPE","DESCRIPTION","","company",null,null,""),
                Job("ID_4","0","TITLE_4","LOCATION_4","TYPE","DESCRIPTION","","company",null,null,"")
        )

        val observer = mock<Observer<List<Job>>>()
        jobsViewModel.jobsResults.observeForever(observer)
        Mockito.`when`(gitHubJobsRepository.searchPosition(anyString()))
                .thenReturn(Observable.just(result))

        Mockito.verifyNoMoreInteractions(observer)
        Mockito.verifyNoMoreInteractions(gitHubJobsRepository)

        jobsViewModel.searchPositions("Test")
        verify(observer).onChanged(result)
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
