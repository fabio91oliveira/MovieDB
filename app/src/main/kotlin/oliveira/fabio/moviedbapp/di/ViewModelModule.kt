package oliveira.fabio.moviedbapp.di

import oliveira.fabio.moviedbapp.feature.moviedetail.viewmodel.MovieDetailViewModel
import oliveira.fabio.moviedbapp.feature.movielist.viewmodel.MovieListViewModel
import oliveira.fabio.moviedbapp.model.SearchParameters
import org.koin.android.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module

val viewModelModule = module {
    single { SearchParameters() }
    viewModel { MovieListViewModel(get(), get()) }
    viewModel { MovieDetailViewModel(get()) }
}