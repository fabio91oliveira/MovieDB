package oliveira.fabio.moviedbapp.moviedetail

import android.content.Intent
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.rule.ActivityTestRule
import br.com.concretesolutions.requestmatcher.RequestMatcherRule
import oliveira.fabio.moviedbapp.R
import oliveira.fabio.moviedbapp.feature.moviedetail.ui.activity.MovieDetailActivity


class MovieDetailRobot(
    private val rule: ActivityTestRule<MovieDetailActivity>,
    private val server: RequestMatcherRule
) {

    companion object {
        private const val MOVIE_ID = "MOVIE_ID"
        private const val ID = 424694
        private const val MOVIE_PATH_REQUEST = "/movie/"
        private const val SERVER_ERROR_CODE = 500
        private const val MOVIE_DETAIL_RESPONSE_JSON = "movie_detail_response.json"
        private const val MOVIE_DETAIL_RESPONSE_JSON_NO_RESULT = "movie_detail_response_no_result.json"
    }

    val intent = Intent()

    fun setupIntentDepends() {
        intent.putExtra(MOVIE_ID, ID)
    }

    fun initIntent() {
        Intents.init()
    }

    fun releaseIntent() {
        Intents.release()
    }


    fun initActivity() {
        rule.launchActivity(intent)
    }

    fun setupMovieDetailRequest() {
        server.addFixture(MOVIE_DETAIL_RESPONSE_JSON)
            .ifRequestMatches()
            .pathIs(MOVIE_PATH_REQUEST + ID)
    }

    fun setupMovieRequestNoResult() {
        server.addFixture(MOVIE_DETAIL_RESPONSE_JSON_NO_RESULT)
            .ifRequestMatches()
            .pathIs(MOVIE_PATH_REQUEST + ID)
    }

    fun setupMovieRequestError() {
        server.addFixture(SERVER_ERROR_CODE, MOVIE_DETAIL_RESPONSE_JSON_NO_RESULT)
            .ifRequestMatches()
            .pathIs(MOVIE_PATH_REQUEST + ID)
    }

    fun shouldShowMovieDetailTextsAndImage() {
        onView(withId(R.id.txtTitle))
            .check(matches(isDisplayed()))
        onView(withId(R.id.txtDuration))
            .check(matches(isDisplayed()))
        onView(withId(R.id.txtBudget))
            .check(matches(isDisplayed()))
        onView(withId(R.id.txtOverview))
            .check(matches(isDisplayed()))
        onView(withId(R.id.imgBack))
            .check(matches(isDisplayed()))
    }

    fun shouldShowErrorMessage() {
        onView(withId(R.id.imgError))
            .check(matches(isDisplayed()))
        onView(withId(R.id.txtErrorMessage))
            .check(matches(isDisplayed()))
    }

}