package com.jurajkusnier.androidapptemplate


import android.content.Intent
import android.os.SystemClock
import android.support.test.InstrumentationRegistry
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.UiController
import android.support.test.espresso.ViewAction
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.*
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import android.support.v7.widget.SearchView
import android.view.View
import com.jurajkusnier.androidapptemplate.CustomMatchers.Companion.withItemCount
import com.jurajkusnier.androidapptemplate.ui.MainActivity
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class HandlingServerResponsesInstrumentedTest {

    @get:Rule
    val activityRule = ActivityTestRule(MainActivity::class.java, true,false)

    private lateinit var mockWebServer: MockWebServer

    private lateinit var app:App

    @Before
    fun setupServer() {

        val instrumentation= InstrumentationRegistry.getInstrumentation()

        app = instrumentation.targetContext.applicationContext as App

        val appInjector = DaggerTestAppComponent.builder()
                .application(app)
                .build()
        appInjector.inject(app)

        mockWebServer = appInjector.getMockWebServer()

        val intent = Intent(InstrumentationRegistry.getInstrumentation()
                .targetContext, MainActivity::class.java)

        activityRule.launchActivity(intent)

    }

    @Test
    fun recycleView_shouldHandleMalformedResponse() {
        mockWebServer.enqueue(MockResponse().setResponseCode(500).setBody("Error"))

        onView(withId(R.id.searchViewJobs)).perform(typeSearchViewText("test"))

        //TODO: don't use sleep
        SystemClock.sleep(1000)

        onView(allOf(withId(android.support.design.R.id.snackbar_text), withText(app.getString(R.string.network_connection_error))))
                .check(matches(isDisplayed()))

    }


    @Test
    fun recycleView_shouldShowThreeElements() {

        mockWebServer.enqueue(MockResponse().setResponseCode(200).setBody(
                """
                    [{
    "id": "123",
    "created_at": "Tue Jul 10 19:20:11 UTC 2018",
    "title": "Ruby Developer",
    "location": "Irvine, CA",
    "type": "Full Time",
    "description": "<p>The Lead Developer (LD) will play a vital role in working on web application features, primarily in the Ruby programming language, as well as maintaining the production web applications.</p>",
    "how_to_apply": "<p>Email your resume to <a href=\"mailto:jobs@sample.com\">jobs@sample.com</a> with the subject \"Lead Ruby Developer [via github]\"</p>",
    "company": "Mock company",
    "company_url": null,
    "company_logo": null,
    "url": "http://jobs.github.com/positions/123"
}, {
    "id": "1234",
    "created_at": "Tue Jul 10 16:45:58 UTC 2018",
    "title": "DevOps Engineer",
    "location": "San Francisco, CA, US",
    "type": "Full Time",
    "description": "<h3>ABC</h3> is a fast growing mobile health startup with headquarters in San Francisco, CA. We provide pharmacies and other health and wellness organizations with patient engagement, medication management, and medication adherence mobile solutions.",
    "how_to_apply": "<p><a href=\"https://www.example.com\">https://www.example.com/</a></p>",
    "company": "ABC",
    "company_url": null,
    "company_logo": null,
    "url": "http://jobs.github.com/positions/1234"
},
{
    "id": "12345",
    "created_at": "Tue Jul 10 16:45:58 UTC 2018",
    "title": "DevOps Engineer",
    "location": "San Francisco, CA, US",
    "type": "Full Time",
    "description": "<h3>ABCD</h3> is a fast growing mobile health startup with headquarters in San Francisco, CA. We provide pharmacies and other health and wellness organizations with patient engagement, medication management, and medication adherence mobile solutions.",
    "how_to_apply": "<p><a href=\"https://www.example.com\">https://www.example.com/</a></p>",
    "company": "ABCD",
    "company_url": null,
    "company_logo": null,
    "url": "http://jobs.github.com/positions/12345"
}]
                """.trimIndent()

        ))

        onView(withId(R.id.searchViewJobs)).perform(typeSearchViewText("test"))

        //TODO: don't use sleep
        SystemClock.sleep(1000)

        onView(withId(R.id.recyclerViewJobs)).check(matches(withItemCount(3)))
    }

}

fun typeSearchViewText(text: String): ViewAction {
    return object : ViewAction {
        override fun getConstraints(): Matcher<View> {
            //Ensure that only apply if it is a SearchView and if it is visible.
            return allOf(isDisplayed(), isAssignableFrom(SearchView::class.java))
        }

        override fun getDescription(): String {
            return "Change view text"
        }

        override fun perform(uiController: UiController, view: View) {
            (view as SearchView).setQuery(text, true)
        }
    }
}