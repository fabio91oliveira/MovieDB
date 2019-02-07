package oliveira.fabio.moviedbapp.feature.movielist.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import oliveira.fabio.moviedbapp.feature.movielist.repository.MovieRepository
import oliveira.fabio.moviedbapp.model.MovieResponse
import oliveira.fabio.moviedbapp.model.Response

class MovieListViewModel(private val movieRepository: MovieRepository) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()
    val movieMutableLiveData = MutableLiveData<Response<MovieResponse>>()

    fun getMovies(sortBy: String, page: String) {
        compositeDisposable.add(
            movieRepository.getMovies(sortBy, page)
                .subscribe({ movieMutableLiveData.postValue(Response.success(it)) },
                    { movieMutableLiveData.postValue(Response.error(it.message, null)) })
        )
    }

    fun onDestroy() = compositeDisposable.takeIf { it.isDisposed }?.run { dispose() }
}