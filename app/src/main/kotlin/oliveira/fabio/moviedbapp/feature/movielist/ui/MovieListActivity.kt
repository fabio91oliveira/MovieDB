package oliveira.fabio.moviedbapp.feature.movielist.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.android.synthetic.main.activity_movie_list.*
import oliveira.fabio.moviedbapp.R
import oliveira.fabio.moviedbapp.feature.movielist.ui.adapter.MovieListAdapter
import oliveira.fabio.moviedbapp.feature.movielist.viewmodel.MovieListViewModel
import oliveira.fabio.moviedbapp.model.Response
import org.koin.android.ext.android.inject

class MovieListActivity : AppCompatActivity() {

    private val viewModel: MovieListViewModel by inject()
    private val adapter by lazy { MovieListAdapter(this) }

    companion object {
        private const val SPAN_COUNT = 2
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_list)
        init()
    }

    private fun init() {
        initLiveDatas()
        initRecyclerView()
        getMovies()
    }

    private fun initLiveDatas() {
        viewModel.movieMutableLiveData.observe(this, Observer {
            when (it.statusEnum) {
                Response.StatusEnum.SUCCESS -> {
                    it?.run {
                        data?.run {
                            adapter.addResult(results)
                            adapter.notifyDataSetChanged()
                        }
                    }
                }
                Response.StatusEnum.ERROR -> {
                    val a = ""
                }
            }
        })
    }

    private fun getMovies() = viewModel.getMovies("popularity.desc", "1")

    private fun initRecyclerView() {
        rvList.layoutManager = GridLayoutManager(this, SPAN_COUNT)
        rvList.adapter = adapter
    }
}
