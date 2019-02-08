package oliveira.fabio.moviedbapp.feature.movielist.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.activity_movie_list.*
import oliveira.fabio.moviedbapp.R
import oliveira.fabio.moviedbapp.feature.moviedetail.ui.activity.MovieDetailActivity
import oliveira.fabio.moviedbapp.feature.moviedetail.ui.activity.MovieDetailActivity.Companion.MOVIE_ID
import oliveira.fabio.moviedbapp.feature.movielist.ui.adapter.MovieListAdapter
import oliveira.fabio.moviedbapp.feature.movielist.viewmodel.MovieListViewModel
import oliveira.fabio.moviedbapp.model.Response
import oliveira.fabio.moviedbapp.util.doRotateAnimation
import org.koin.android.viewmodel.ext.android.viewModel


class MovieListActivity : AppCompatActivity(), MovieListAdapter.OnClickMovieListener {
    private val viewModel: MovieListViewModel by viewModel()
    private val adapter by lazy { MovieListAdapter(this) }

    companion object {
        private const val SPAN_COUNT = 2
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_list)

        if (savedInstanceState == null) {
            init()
        } else {
            initToolbar()
            initLiveDatas()
            initRecyclerView()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.onDestroy()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.toolbar_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_about -> {
                showAboutDialog()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun setOnClickMovieListener(id: Int) = startActivity(
        Intent(this, MovieDetailActivity::class.java)
            .putExtra(MOVIE_ID, id)
    )

    private fun init() {
        showLoading()
        initToolbar()
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
                            hideLoading()
                        }
                    }
                }
                Response.StatusEnum.ERROR -> {
                    val a = ""
                }
            }
        })
    }

    private fun initToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
    }

    private fun getMovies() = viewModel.getMovies("popularity.desc", "1")

    private fun initRecyclerView() {
        rvList.layoutManager = GridLayoutManager(
            this,
            SPAN_COUNT
        )
        rvList.adapter = adapter
    }

    private fun showAboutDialog() {
        val dialog = BottomSheetDialog(this)
        val bottomSheet = layoutInflater.inflate(R.layout.dialog_bottom_about, null)
        bottomSheet.setOnClickListener { dialog.dismiss() }
        dialog.setContentView(bottomSheet)
        dialog.show()
    }

    private fun showLoading() {
        loading.doRotateAnimation()
        loading.visibility = VISIBLE
        rvList.visibility = GONE
        toolbar.visibility = GONE
    }

    private fun hideLoading() {
        loading.clearAnimation()
        loading.visibility = GONE
        rvList.visibility = VISIBLE
        toolbar.visibility = VISIBLE
    }
}
