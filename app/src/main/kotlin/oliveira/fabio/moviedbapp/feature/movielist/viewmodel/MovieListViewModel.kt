package oliveira.fabio.moviedbapp.feature.movielist.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import oliveira.fabio.moviedbapp.feature.movielist.repository.MovieListRepository
import oliveira.fabio.moviedbapp.model.MoviesResponse
import oliveira.fabio.moviedbapp.model.Response
import oliveira.fabio.moviedbapp.model.SearchParameters
import oliveira.fabio.moviedbapp.util.Event

class MovieListViewModel(private val repository: MovieListRepository) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()
    val movieMutableLiveData = MutableLiveData<Event<Response<MoviesResponse>>>()
    val searchParameters = SearchParameters()

    fun isPageEqualsOne() = searchParameters.page == 1

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

    fun onDestroy() = compositeDisposable.takeIf { it.isDisposed }?.run { dispose() }

}