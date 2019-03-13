package oliveira.fabio.moviedbapp.feature.movielist.ui.activity

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.MenuItem
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.MenuItemCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.android.synthetic.main.activity_movie_list.*
import oliveira.fabio.moviedbapp.R
import oliveira.fabio.moviedbapp.feature.moviedetail.ui.activity.MovieDetailActivity
import oliveira.fabio.moviedbapp.feature.moviedetail.ui.activity.MovieDetailActivity.Companion.MOVIE_ID
import oliveira.fabio.moviedbapp.feature.movielist.ui.adapter.MovieListAdapter
import oliveira.fabio.moviedbapp.feature.movielist.ui.listener.InfiniteScrollListener
import oliveira.fabio.moviedbapp.feature.movielist.viewmodel.MovieListViewModel
import oliveira.fabio.moviedbapp.model.MoviesResponse
import oliveira.fabio.moviedbapp.model.Response
import oliveira.fabio.moviedbapp.util.extensions.doRotateAnimation
import org.koin.android.viewmodel.ext.android.viewModel


class MovieListActivity : AppCompatActivity(), MovieListAdapter.OnClickMovieListener, TextWatcher {
    override fun afterTextChanged(s: Editable?) {}

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
        if (s.isNotEmpty()) {
            unselectTabs()
            viewModel.setSearchByText(true)
            viewModel.getMovieByText(s.toString())
        }
    }

    private val viewModel: MovieListViewModel by viewModel()
    private val adapter by lazy { MovieListAdapter(this) }

    companion object {
        private var SPAN_COUNT = 3
        private const val CURRENT_TAB = "CURRENT_TAB"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_list)
        setSpanConsideringOrientation()

        if (savedInstanceState == null) {
            init()
        } else {
            if (savedInstanceState.getInt(CURRENT_TAB) != null) tabLayout.getTabAt(savedInstanceState.getInt(CURRENT_TAB))?.select()
            setupTabLayout()
            initClickListeners()
            initLiveDatas()
            initRecyclerView()
            initSearchViewListener()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(CURRENT_TAB, tabLayout.selectedTabPosition)
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.onDestroy()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.movieSearch -> {
                MenuItemCompat.expandActionView(item)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun setOnClickMovieListener(id: Int) = startActivity(
        Intent(this, MovieDetailActivity::class.java)
            .putExtra(MOVIE_ID, id)
    )

    override fun onBackPressed() {
        when (searchView.isVisible()) {
            true -> searchView.closeSearch()
            false -> super.onBackPressed()
        }
    }

    private fun init() {
        hideErrorMessage()
        hideContent()
        showLoading()
        setupTabLayout()
        initClickListeners()
        initLiveDatas()
        initRecyclerView()
        initSearchViewListener()
        getMovies()
    }

    private fun setSpanConsideringOrientation() {
        SPAN_COUNT = when (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            true -> 5
            else -> 3
        }
    }

    private fun initClickListeners() {
        imgError.setOnClickListener {
            hideErrorMessage()
            hideContent()
            showLoading()
            getMovies()
        }
        txtErrorMessage.setOnClickListener {
            hideErrorMessage()
            hideContent()
            showLoading()
            getMovies()
        }
    }

    private fun initSearchViewListener() = searchView.setTextWatcherListener(this)

    private fun initLiveDatas() {
        viewModel.movieMutableLiveData.observe(this, Observer { event ->
            event.getContentIfNotHandled()?.let {
                when (it.statusEnum) {
                    Response.StatusEnum.SUCCESS -> {
                        it?.run {
                            data?.run {
                                when (results.isNotEmpty()) {
                                    true -> {
                                        if (viewModel.isSearchByText()) clearResultList()
                                        viewModel.searchParameters.addResults(results)
                                        addResult(results)
                                        hideErrorMessage()
                                        showContent()
                                    }
                                    else -> {
                                        hideContent()
                                        showErrorMessage(R.string.movie_list_no_results_message)
                                    }
                                }
                            }
                        }
                    }
                    Response.StatusEnum.ERROR -> {
                        if (viewModel.isPageEqualsOne()) {
                            hideContent()
                            showErrorMessage(R.string.movie_error_message)
                        }
                    }
                }
                hideLoading()
            }
        })
    }

    private fun getMovies() = viewModel.getMovies()

    private fun initRecyclerView() {
        val layoutManager = GridLayoutManager(this, SPAN_COUNT)
        rvList.layoutManager = layoutManager
        rvList.adapter = adapter
        rvList.addOnScrollListener(object : InfiniteScrollListener(layoutManager, viewModel.searchParameters.page) {
            override fun onLoadMore(page: Int) {
                if (!viewModel.isSearchByText()) {
                    viewModel.searchParameters.page = page
                    getMovies()
                }
            }
        })
        if (viewModel.searchParameters.results.isNotEmpty()) {
            addResult(viewModel.searchParameters.results)
            hideErrorMessage()
            hideLoading()
            showContent()
        }
    }

    private fun setupTabLayout() {
        for (i in 0 until tabLayout.tabCount) {
            val tab = (tabLayout.getChildAt(0) as ViewGroup).getChildAt(i)
            val p = tab.layoutParams as ViewGroup.MarginLayoutParams
            tab.requestLayout()
            tab.setOnClickListener {
                when (tab.contentDescription) {
                    resources.getString(R.string.movie_list_tab_popular) -> {
                        viewModel.searchParameters.setPopularSort()
                    }
                    resources.getString(R.string.movie_list_tab_release_date) -> {
                        viewModel.searchParameters.setReleaseDateSort()
                    }
                    resources.getString(R.string.movie_list_tab_budget) -> {
                        viewModel.searchParameters.setBudgetSort()
                    }
                    resources.getString(R.string.movie_list_tab_vote) -> {
                        viewModel.searchParameters.setVoteSort()
                    }
                }
                tab.isSelected = true
                changeTabLayoutSelectedTabColor(R.color.colorRed)
                viewModel.setSearchByText(false)
                viewModel.searchParameters.resetPageCount()
                clearResultList()
                hideErrorMessage()
                hideContent()
                showLoading()
                initRecyclerView()
                getMovies()
            }
        }
    }

    private fun unselectTabs() {
        changeTabLayoutSelectedTabColor(R.color.colorPrimary)
        for (i in 0 until tabLayout.tabCount) {
            val tab = (tabLayout.getChildAt(0) as ViewGroup).getChildAt(i)
            tab.isSelected = false
        }
    }

    private fun addResult(results: List<MoviesResponse.Result>) {
        adapter.addResult(results)
        adapter.notifyDataSetChanged()
    }

    private fun clearResultList() {
        viewModel.searchParameters.clearResults()
        adapter.clearList()
        adapter.notifyDataSetChanged()
    }

    private fun showErrorMessage(resourceString: Int) {
        txtErrorMessage.text = resources.getString(resourceString)
        errorGroup.visibility = VISIBLE
    }

    private fun hideErrorMessage() {
        errorGroup.visibility = GONE
    }

    private fun showLoading() {
        loading.doRotateAnimation()
        loading.visibility = VISIBLE
    }

    private fun showContent() {
        rvList.visibility = VISIBLE
    }

    private fun hideContent() {
        rvList.visibility = GONE
    }

    private fun hideLoading() {
        loading.clearAnimation()
        loading.visibility = GONE
    }

    private fun changeTabLayoutSelectedTabColor(color: Int) =
        tabLayout.setSelectedTabIndicatorColor(ContextCompat.getColor(this, color))

}
