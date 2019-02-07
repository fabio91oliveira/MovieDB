package oliveira.fabio.moviedbapp.feature.movielist.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import oliveira.fabio.moviedbapp.feature.movielist.repository.MovieRepository
import oliveira.fabio.moviedbapp.feature.movielist.viewmodel.MovieListViewModel

class MovieListViewModelFactory(private val repository: MovieRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return when {
            (modelClass.isAssignableFrom(MovieListViewModel::class.java)) -> MovieListViewModel(repository) as T
            else -> throw IllegalArgumentException("ViewModel Not Found")
        }
    }
}