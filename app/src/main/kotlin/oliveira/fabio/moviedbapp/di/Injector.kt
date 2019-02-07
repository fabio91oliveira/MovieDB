package oliveira.fabio.moviedbapp.di

import oliveira.fabio.moviedbapp.di.dependency.RepositoryDependency
import oliveira.fabio.moviedbapp.feature.movielist.factory.MovieListViewModelFactory

object Injector {
    fun provideMovieListViewModelFactory() =
        MovieListViewModelFactory(RepositoryDependency.provideMovieRepository())
}