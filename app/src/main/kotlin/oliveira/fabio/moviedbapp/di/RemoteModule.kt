package oliveira.fabio.moviedbapp.di

import oliveira.fabio.moviedbapp.network.MovieApi
import oliveira.fabio.moviedbapp.network.config.provideApi
import oliveira.fabio.moviedbapp.util.BASE_URL_REQUEST
import org.koin.dsl.module.module

val remoteModule = module {
    single { provideApi(MovieApi::class.java, BASE_URL_REQUEST) }
}