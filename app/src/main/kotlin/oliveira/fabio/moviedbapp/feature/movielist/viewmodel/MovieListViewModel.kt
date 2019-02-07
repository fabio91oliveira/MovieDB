package oliveira.fabio.moviedbapp.feature.movielist.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import oliveira.fabio.moviedbapp.feature.movielist.repository.MovieRepository
import oliveira.fabio.moviedbapp.model.MovieResponse
import oliveira.fabio.moviedbapp.model.Response
import retrofit2.HttpException

class MovieListViewModel(private val movieRepository: MovieRepository) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()
    val movieMutableLiveData = MutableLiveData<Response<MovieResponse>>()

    fun getMovies(sortBy: String, page: String) {
        compositeDisposable.add(
            movieRepository.getMovies(sortBy, page)
                .subscribe({
                    movieMutableLiveData.postValue(Response.success(it))
                }, {
                    var code: String? = null
                    if (it is HttpException) {
                        code = it.code().toString()
                    }
                    movieMutableLiveData.postValue(Response.error(it.message, code, null))
                })
        )
    }

    fun onDestroy() = compositeDisposable.takeIf { it.isDisposed }?.run { dispose() }
}