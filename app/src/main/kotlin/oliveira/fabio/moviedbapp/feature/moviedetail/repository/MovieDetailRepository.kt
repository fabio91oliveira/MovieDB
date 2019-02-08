package oliveira.fabio.moviedbapp.feature.moviedetail.repository

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import oliveira.fabio.moviedbapp.network.MovieApi

class MovieDetailRepository(private val api: MovieApi) {
    fun getMovieById(id: Int) = api.getMovieDetail(id)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
}