package oliveira.fabio.moviedbapp.feature.movielist.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
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
import oliveira.fabio.moviedbapp.model.SearchParameters
import oliveira.fabio.moviedbapp.util.doRotateAnimation
import org.koin.android.viewmodel.ext.android.viewModel


class MovieListActivity : AppCompatActivity(), MovieListAdapter.OnClickMovieListener {

    private val viewModel: MovieListViewModel by viewModel()
    private val adapter by lazy { MovieListAdapter(this) }
    private lateinit var searchParameters: SearchParameters

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
            setupTabLayout()
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
        initSearchParameter()
        showLoading()
        initToolbar()
        setupTabLayout()
        initLiveDatas()
        initRecyclerView()
        getMovies()
    }

    private fun initSearchParameter() {
        searchParameters = SearchParameters()
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

    private fun getMovies() = viewModel.getMovies(searchParameters)

    private fun initRecyclerView() {
        rvList.layoutManager = GridLayoutManager(
                this,
                SPAN_COUNT
        )
        rvList.adapter = adapter
    }

    private fun setupTabLayout() {
        for (i in 0 until tabLayout.tabCount) {
            val tab = (tabLayout.getChildAt(0) as ViewGroup).getChildAt(i)
            val p = tab.layoutParams as ViewGroup.MarginLayoutParams
            p.setMargins(50, 0, 0, 0)
            tab.requestLayout()
            tab.setOnClickListener {
                when (tab.contentDescription) {
                    resources.getString(R.string.movie_list_tab_popular) -> {
                        searchParameters.setPopularSort()
                    }
                    resources.getString(R.string.movie_list_tab_release_date) -> {
                        searchParameters.setReleaseDateSort()
                    }
                    resources.getString(R.string.movie_list_tab_budget) -> {
                        searchParameters.setBudgetSort()
                    }
                    resources.getString(R.string.movie_list_tab_vote) -> {
                        searchParameters.setVoteSort()
                    }
                }
                clearResultList()
                showLoading()
                getMovies()
            }
        }
    }

    private fun clearResultList() {
        adapter.clearList()
        adapter.notifyDataSetChanged()
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
    }

    private fun hideLoading() {
        loading.clearAnimation()
        loading.visibility = GONE
        rvList.visibility = VISIBLE
    }
}
