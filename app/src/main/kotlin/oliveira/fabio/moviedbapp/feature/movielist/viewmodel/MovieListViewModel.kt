package oliveira.fabio.moviedbapp.feature.movielist.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import oliveira.fabio.moviedbapp.feature.movielist.repository.MovieListRepository
import oliveira.fabio.moviedbapp.model.MoviesResponse
import oliveira.fabio.moviedbapp.model.Response
import oliveira.fabio.moviedbapp.model.SearchParameters
import oliveira.fabio.moviedbapp.util.Event

class MovieListViewModel(private val repository: MovieListRepository, var searchParameters: SearchParameters) :
    ViewModel() {

    companion object {
        private const val EMPTY_STRING = ""
    }

    private val compositeDisposable = CompositeDisposable()
    val movieMutableLiveData = MutableLiveData<Event<Response<MoviesResponse>>>()

    fun isPageEqualsOne() = searchParameters.page == 1

    fun isSearchByText() = searchParameters.isQuerySearch

    fun setSearchByText(status: Boolean) {
        searchParameters.queryMovie = EMPTY_STRING
        searchParameters.isQuerySearch = status
    }

    fun getMovies() {
        compositeDisposable.add(
            repository.getMovies(searchParameters.sortyBy, searchParameters.page)
                .subscribe({
                    movieMutableLiveData.value = Event(Response.success(it))
                },
                    {
                        movieMutableLiveData.value = Event(Response.error(it.message, null))
                    })
        )
    }

    fun getMovieByText(query: String) {
        searchParameters.queryMovie = query
        compositeDisposable.add(
            repository.getMovieByText(searchParameters.queryMovie)
                .subscribe({
                    movieMutableLiveData.value = Event(Response.success(it))
                },
                    {
                        movieMutableLiveData.value = Event(Response.error(it.message, null))
                    })
        )
    }

    fun onDestroy() = compositeDisposable.takeIf { it.isDisposed }?.run { dispose() }

}