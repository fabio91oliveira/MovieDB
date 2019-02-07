package oliveira.fabio.moviedbapp.di

import oliveira.fabio.moviedbapp.feature.movielist.repository.MovieRepository
import org.koin.dsl.module.module

val repositoryModule = module {
    single { MovieRepository(get()) }
}