package oliveira.fabio.moviedbapp.network

import io.reactivex.Flowable
import oliveira.fabio.moviedbapp.model.MovieResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface MovieApi {
    @GET("discover/movie")
    fun getMovieList(@Query("sort_by") sortBy: String, @Query("page") page: String): Flowable<MovieResponse>
}