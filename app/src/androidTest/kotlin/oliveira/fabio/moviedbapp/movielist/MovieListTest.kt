package oliveira.fabio.moviedbapp.movielist

import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import oliveira.fabio.moviedbapp.base.BaseTest
import oliveira.fabio.moviedbapp.di.testRemoteModule
import oliveira.fabio.moviedbapp.feature.movielist.ui.activity.MovieListActivity
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.standalone.StandAloneContext.loadKoinModules

@RunWith(AndroidJUnit4::class)
class MovieListTest : BaseTest() {

    @Rule
    @JvmField
    val rule = ActivityTestRule<MovieListActivity>(MovieListActivity::class.java, false, false)

    private fun robots(func: MovieListRobot.() -> Unit) = MovieListRobot(rule, server).apply(func)

    @Before
    fun setup() {
        loadKoinModules(testRemoteModule)
    }

    @Test
    fun shouldExistAtLeastOneItem() {
        robots {
            setupMovieRequest()
            initActivity()
            shouldRecyclerViewDisplay()
        }
    }

    @Test
    fun shouldNotExistItems() {
        robots {
            setupMovieRequestNoResult()
            initActivity()
            shouldShouldNoResultMessage()
        }
    }

    @Test
    fun shouldAppearMovieDetailScreen() {
        robots {
            initIntent()
            setupMovieRequest()
            setupMovieDetailRequest()
            initActivity()
            shouldClickFirstItem()
            shouldMovieDetailActivityOpen()
            releaseIntent()
        }
    }

    @Test
    fun shouldSearchByTextAndExistAtLeastOneItem() {
        robots {
            setupMovieRequest()
            setupSearchRequest()
            initActivity()
            shouldClickSearchButtonMenu()
            shouldTypeOnSearchViewInput()
            shouldRecyclerViewDisplay()
        }
    }

    @Test
    fun shouldShowErrorScreen() {
        robots {
            setupMovieRequestError()
            initActivity()
            shouldShouldErrorMessage()
        }
    }
}