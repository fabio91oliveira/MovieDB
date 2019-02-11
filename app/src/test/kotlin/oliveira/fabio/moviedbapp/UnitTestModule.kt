package oliveira.fabio.moviedbapp

import oliveira.fabio.moviedbapp.feature.movielist.repository.MovieListRepository
import oliveira.fabio.moviedbapp.feature.movielist.viewmodel.MovieListViewModel
import oliveira.fabio.moviedbapp.model.SearchParameters
import oliveira.fabio.moviedbapp.network.MovieApi
import org.koin.android.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module
import org.mockito.Mockito

val unitTestModule = module {
    single { MovieListRepository(Mockito.mock(MovieApi::class.java)) }
    single { SearchParameters() }
    viewModel { MovieListViewModel(get(), get()) }
}