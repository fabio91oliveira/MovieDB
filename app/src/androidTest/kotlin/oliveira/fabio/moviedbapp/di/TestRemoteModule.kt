package oliveira.fabio.moviedbapp.di

import oliveira.fabio.moviedbapp.base.TestUrl
import oliveira.fabio.moviedbapp.network.MovieApi
import oliveira.fabio.moviedbapp.network.config.provideApi
import org.koin.dsl.module.module

val testRemoteModule = module {
    single(override = true) { provideApi(MovieApi::class.java, TestUrl.urlTest) }
}