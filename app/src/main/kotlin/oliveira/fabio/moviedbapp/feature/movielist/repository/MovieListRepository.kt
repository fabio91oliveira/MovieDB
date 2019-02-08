package oliveira.fabio.moviedbapp.feature.movielist.repository

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import oliveira.fabio.moviedbapp.network.MovieApi

class MovieListRepository(private val api: MovieApi) {
    fun getMovies(sortBy: String, page: String) = api.getMovieList(sortBy, page)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
}