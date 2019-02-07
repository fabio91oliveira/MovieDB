package oliveira.fabio.moviedbapp.di.dependency

import oliveira.fabio.moviedbapp.feature.movielist.repository.MovieRepository

object RepositoryDependency {
    fun provideMovieRepository() = MovieRepository(ApiDependency.provideMovieApi())
}