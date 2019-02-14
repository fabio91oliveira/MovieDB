package oliveira.fabio.moviedbapp

import oliveira.fabio.moviedbapp.feature.movielist.viewmodel.MovieListViewModel
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.koin.standalone.StandAloneContext.closeKoin
import org.koin.standalone.StandAloneContext.startKoin
import org.koin.standalone.inject
import org.koin.test.KoinTest

class MovieListViewModelTest : KoinTest {

    private val movieListViewModel: MovieListViewModel by inject()

    @Before
    fun before() {
        startKoin(listOf(unitTestModule))
    }

    @Test
    fun shouldPageStartsAtOne() {
        Assert.assertEquals(movieListViewModel.isPageEqualsOne(), true)
    }

    @Test
    fun shouldPageStartsMoreThanOne() {
        movieListViewModel.searchParameters.page = 2
        Assert.assertEquals(movieListViewModel.isPageEqualsOne(), false)
    }

    @Test
    fun shouldGetReturnTrueFromStatusQuery() {
        movieListViewModel.searchParameters.isQuerySearch = true
        Assert.assertEquals(movieListViewModel.isSearchByText(), true)
    }

    @Test
    fun shouldGetReturnFalseFromStatusQuery() {
        movieListViewModel.searchParameters.isQuerySearch = false
        Assert.assertEquals(movieListViewModel.isSearchByText(), false)
    }

    @Test
    fun shouldChangeStatusOfParameter() {
        movieListViewModel.setSearchByText(false)
    }

    @After
    fun after() {
        closeKoin()
    }

}