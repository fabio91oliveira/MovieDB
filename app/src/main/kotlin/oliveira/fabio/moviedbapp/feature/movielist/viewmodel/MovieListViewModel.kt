package oliveira.fabio.moviedbapp.feature.movielist.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import oliveira.fabio.moviedbapp.feature.movielist.repository.MovieListRepository
import oliveira.fabio.moviedbapp.model.MoviesResponse
import oliveira.fabio.moviedbapp.model.Response

class MovieListViewModel(private val repository: MovieListRepository) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()
    val movieMutableLiveData = MutableLiveData<Response<MoviesResponse>>()

    fun getMovies(sortBy: String, page: String) {
        compositeDisposable.add(
            repository.getMovies(sortBy, page)
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