package oliveira.fabio.moviedbapp.di

import oliveira.fabio.moviedbapp.feature.moviedetail.repository.MovieDetailRepository
import oliveira.fabio.moviedbapp.feature.movielist.repository.MovieListRepository
import org.koin.dsl.module.module

val repositoryModule = module {
    single { MovieListRepository(get()) }
    single { MovieDetailRepository(get()) }
}