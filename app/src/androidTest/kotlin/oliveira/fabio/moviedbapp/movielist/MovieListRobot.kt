package oliveira.fabio.moviedbapp.movielist

import android.content.Intent
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.rule.ActivityTestRule
import br.com.concretesolutions.requestmatcher.RequestMatcherRule
import oliveira.fabio.moviedbapp.R
import oliveira.fabio.moviedbapp.feature.moviedetail.ui.activity.MovieDetailActivity
import oliveira.fabio.moviedbapp.feature.movielist.ui.activity.MovieListActivity


class MovieListRobot(private val rule: ActivityTestRule<MovieListActivity>, private val server: RequestMatcherRule) {

    companion object {
        private const val MOVIE_LIST_REQUEST_PATH = "/discover/movie"
        private const val MOVIE_LIST_SEARCH_REQUEST_PATH = "/search/movie"
        private const val MOVIE_LIST_RESPONSE_JSON = "movie_list_response.json"
        private const val MOVIE_LIST_RESPONSE_JSON_NO_RESULT = "movie_list_response_no_result.json"
        private const val MOVIE_DETAIL_RESPONSE_JSON = "movie_detail_response.json"

        private const val MOVIE_PATH_REQUEST = "/movie/"
        private const val MOVIE_ID = "424694"
        private const val PARAMETER_FOR_RESEARCH = "t"
    }

    val intent = Intent()

    fun initActivity() {
        rule.launchActivity(intent)
    }

    fun initIntent() {
        Intents.init()
    }

    fun releaseIntent() {
        Intents.release()
    }

    fun setupMovieRequest() {
        server.addFixture(MOVIE_LIST_RESPONSE_JSON)
            .ifRequestMatches()
            .pathIs(MOVIE_LIST_REQUEST_PATH)
    }

    fun setupMovieRequestNoResult() {
        server.addFixture(MOVIE_LIST_RESPONSE_JSON_NO_RESULT)
            .ifRequestMatches()
            .pathIs(MOVIE_LIST_REQUEST_PATH)
    }

    fun setupMovieRequestError() {
        server.addFixture(500, MOVIE_LIST_RESPONSE_JSON_NO_RESULT)
            .ifRequestMatches()
            .pathIs(MOVIE_LIST_REQUEST_PATH)
    }

    fun setupMovieDetailRequest() {
        server.addFixture(MOVIE_DETAIL_RESPONSE_JSON)
            .ifRequestMatches()
            .pathIs(MOVIE_PATH_REQUEST + MOVIE_ID)
    }

    fun setupSearchRequest() {
        server.addFixture(MOVIE_LIST_RESPONSE_JSON)
            .ifRequestMatches()
            .pathIs(MOVIE_LIST_SEARCH_REQUEST_PATH)
    }

    fun shouldRecyclerViewDisplay() {
        onView(withId(R.id.rvList)).check(matches(isDisplayed()))
    }

    fun shouldShouldNoResultMessage() {
        onView(withId(R.id.imgError)).check(matches(isDisplayed()))
        onView(withId(R.id.txtErrorMessage)).check(matches(withText(R.string.movie_list_no_results_message)))
    }

    fun shouldShouldErrorMessage() {
        onView(withId(R.id.imgError)).check(matches(isDisplayed()))
        onView(withId(R.id.txtErrorMessage)).check(matches(withText(R.string.movie_error_message)))
    }

    fun shouldClickFirstItem() {
        onView(withId(R.id.rvList))
            .perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(1, click()))
    }

    fun shouldMovieDetailActivityOpen() {
        intended(hasComponent(MovieDetailActivity::class.java.name))
    }

    fun shouldClickSearchButtonMenu() {
        onView(withId(R.id.movieSearch))
            .perform(click())
    }

    fun shouldTypeOnSearchViewInput() {
        onView(withId(androidx.appcompat.R.id.search_src_text))
            .perform(typeText(PARAMETER_FOR_RESEARCH))
    }
}