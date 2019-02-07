package oliveira.fabio.moviedbapp.feature.movielist.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import oliveira.fabio.moviedbapp.R
import oliveira.fabio.moviedbapp.di.Injector
import oliveira.fabio.moviedbapp.feature.movielist.viewmodel.MovieListViewModel
import oliveira.fabio.moviedbapp.model.Response

class MovieListActivity : AppCompatActivity() {

    private val viewModel by lazy {
        ViewModelProviders.of(this, Injector.provideMovieListViewModelFactory()).get(MovieListViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_list)
        initLiveDatas()
        viewModel.getMovies("popularity.desc", "1")
    }

    private fun initLiveDatas() {
        viewModel.movieMutableLiveData.observe(this, Observer {
            when (it.statusEnum) {
                Response.StatusEnum.SUCCESS -> {
                    val a = ""
                }
                Response.StatusEnum.ERROR -> {
                    val a = ""
                }
            }
        })
    }
}
