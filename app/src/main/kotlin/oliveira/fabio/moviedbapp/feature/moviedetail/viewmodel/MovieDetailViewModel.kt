package oliveira.fabio.moviedbapp.feature.moviedetail.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import oliveira.fabio.moviedbapp.feature.moviedetail.repository.MovieDetailRepository
import oliveira.fabio.moviedbapp.model.MovieDetailResponse
import oliveira.fabio.moviedbapp.model.Response

class MovieDetailViewModel(private val repository: MovieDetailRepository) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()
    val movieDetailMutableLiveData = MutableLiveData<Response<MovieDetailResponse>>()

    fun getMovieDetail(id: Int) {
        compositeDisposable.add(
            repository.getMovieById(id)
                .subscribe({
                    movieDetailMutableLiveData.postValue(Response.success(it))
                }, {
                    movieDetailMutableLiveData.postValue(Response.error(it.message, null))
                })
        )
    }

    fun onDestroy() = compositeDisposable.takeIf { it.isDisposed }?.run { dispose() }

}