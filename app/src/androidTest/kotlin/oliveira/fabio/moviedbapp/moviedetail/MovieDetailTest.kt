package oliveira.fabio.moviedbapp.moviedetail

import androidx.test.rule.ActivityTestRule
import oliveira.fabio.moviedbapp.base.BaseTest
import oliveira.fabio.moviedbapp.di.testRemoteModule
import oliveira.fabio.moviedbapp.feature.moviedetail.ui.activity.MovieDetailActivity
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.koin.standalone.StandAloneContext.loadKoinModules

class MovieDetailTest : BaseTest() {

    @Rule
    @JvmField
    val rule = ActivityTestRule<MovieDetailActivity>(MovieDetailActivity::class.java, false, false)

    private fun robots(func: MovieDetailRobot.() -> Unit) = MovieDetailRobot(rule, server).apply(func)

    @Before
    fun setup() {
        loadKoinModules(testRemoteModule)
    }

    @Test
    fun shouldLoadMovieDetailFromRequest() {
        robots {
            initIntent()
            setupIntentDepends()
            setupMovieDetailRequest()
            initActivity()
            shouldShowMovieDetailTextsAndImage()
            releaseIntent()
        }
    }

    @Test
    fun shouldShowErrorMessageFromNoResultRequest() {
        robots {
            initIntent()
            setupIntentDepends()
            setupMovieRequestNoResult()
            initActivity()
            shouldShowErrorMessage()
            releaseIntent()
        }
    }

    @Test
    fun shouldShowErrorMessageFromErrorRequest() {
        robots {
            initIntent()
            setupIntentDepends()
            setupMovieRequestError()
            initActivity()
            shouldShowErrorMessage()
            releaseIntent()
        }
    }

}