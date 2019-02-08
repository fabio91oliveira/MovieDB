package oliveira.fabio.moviedbapp.network

import io.reactivex.Flowable
import oliveira.fabio.moviedbapp.model.MovieDetailResponse
import oliveira.fabio.moviedbapp.model.MoviesResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieApi {
    @GET("discover/movie")
    fun getMovieList(@Query("sort_by") sortBy: String, @Query("page") page: Int): Flowable<MoviesResponse>

    @GET("movie/{id}")
    fun getMovieDetail(@Path("id") id: Int): Flowable<MovieDetailResponse>
}