package oliveira.fabio.moviedbapp.feature.movielist.ui.activity

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.view.*
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.SearchView
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


class MovieListActivity : AppCompatActivity(), MovieListAdapter.OnClickMovieListener {

    private val viewModel: MovieListViewModel by viewModel()
    private val adapter by lazy { MovieListAdapter(this) }

    private lateinit var removeButton: AppCompatImageView
    private lateinit var searchPlate: View
    private lateinit var searchView: SearchView
    private lateinit var searchAutoComplete: SearchView.SearchAutoComplete

    companion object {
        private var SPAN_COUNT = 3
        private const val CURRENT_TAB = "CURRENT_TAB"
        private const val EMPTY_TEXT = ""
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_list)
        setSpanConsideringOrientation()

        if (savedInstanceState == null) {
            init()
        } else {
            initToolbar()
            if (savedInstanceState.getInt(CURRENT_TAB) != null) tabLayout.getTabAt(savedInstanceState.getInt(CURRENT_TAB))?.select()
            setupTabLayout()
            initClickListeners()
            initLiveDatas()
            initRecyclerView()
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

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.toolbar_menu, menu)

        val menuItem = menu.findItem(R.id.movieSearch)
        searchView = menuItem?.actionView as SearchView
        searchPlate = searchView.findViewById(androidx.appcompat.R.id.search_plate) as View
        removeButton = searchView.findViewById(androidx.appcompat.R.id.search_close_btn)
        removeButton.setOnClickListener {
            clearSearchText()
            changeStatusRemoveButton(false)
        }
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(s: String): Boolean {
                if (s.isNotEmpty()) {
                    unselectTabs()
                    viewModel.setSearchByText(true)
                    viewModel.getMovieByText(s)
                    loading(true)
                }
                return false
            }
        })
        searchAutoComplete = searchView.findViewById(androidx.appcompat.R.id.search_src_text)
        val ssb = SpannableStringBuilder(EMPTY_TEXT)
        ssb.append(EMPTY_TEXT)
        searchAutoComplete.hint = resources.getString(R.string.movie_list_search_text)
        searchAutoComplete.setTextColor(ContextCompat.getColor(this, R.color.colorWhite))
        return super.onCreateOptionsMenu(menu)
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

    private fun init() {
        hideErrorMessage()
        hideContent()
        showLoading()
        initToolbar()
        setupTabLayout()
        initClickListeners()
        initLiveDatas()
        initRecyclerView()
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
                loading(false)
                hideLoading()
            }
        })
    }

    private fun initToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
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
                clearSearchText()
                searchView.onActionViewCollapsed()
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


    private fun loading(isLoading: Boolean) {
        if (isLoading && !searchAutoComplete.text.isEmpty()) {
            changeStatusRemoveButton(false)
            if (searchPlate.findViewById<View>(R.id.search_progress_bar) != null)
                searchPlate.findViewById<View>(R.id.search_progress_bar).animate().setDuration(200).alpha(1f).start()
            else {
                val v = LayoutInflater.from(this).inflate(R.layout.icon_loading, null)
                (searchPlate as ViewGroup).addView(v, 2)
            }
        } else {
            if (searchPlate.findViewById<View>(R.id.search_progress_bar) != null) {
                searchPlate.findViewById<View>(R.id.search_progress_bar).animate().setDuration(200).alpha(0f).start()
                (searchPlate as ViewGroup).removeViewAt(2)
                changeStatusRemoveButton(true)
            }
        }
    }

    private fun changeStatusRemoveButton(isVisible: Boolean) {
        removeButton.visibility = if (isVisible) View.VISIBLE else View.GONE
    }

    private fun clearSearchText() = searchAutoComplete.setText("")

    private fun changeTabLayoutSelectedTabColor(color: Int) =
        tabLayout.setSelectedTabIndicatorColor(ContextCompat.getColor(this, color))

}
