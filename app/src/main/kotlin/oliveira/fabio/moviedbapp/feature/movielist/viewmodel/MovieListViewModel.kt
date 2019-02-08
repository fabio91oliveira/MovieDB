package oliveira.fabio.moviedbapp.feature.movielist.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import oliveira.fabio.moviedbapp.feature.movielist.repository.MovieListRepository
import oliveira.fabio.moviedbapp.model.MoviesResponse
import oliveira.fabio.moviedbapp.model.Response
import oliveira.fabio.moviedbapp.model.SearchParameters

class MovieListViewModel(private val repository: MovieListRepository) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()
    val movieMutableLiveData = MutableLiveData<Response<MoviesResponse>>()
    val searchParameters = SearchParameters()

    fun getMovies() {
        compositeDisposable.add(
            repository.getMovies(searchParameters.sortyBy, searchParameters.page)
                .subscribe({
                    movieMutableLiveData.postValue(Response.success(it))
                },
                    {
                        movieMutableLiveData.postValue(Response.error(it.message, null))
                    })
        )
    }

    fun onDestroy() = compositeDisposable.takeIf { it.isDisposed }?.run { dispose() }

}