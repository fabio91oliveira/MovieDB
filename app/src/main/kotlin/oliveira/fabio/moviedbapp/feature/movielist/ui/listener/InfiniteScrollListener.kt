package oliveira.fabio.moviedbapp.feature.movielist.ui.listener

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

abstract class InfiniteScrollListener(
    private val layoutManager: RecyclerView.LayoutManager,
    private var currentPage: Int
) :
    RecyclerView.OnScrollListener() {

    private val visibleThreshold = 5
    private var previousTotalItemCount = 0
    private var firstVisibleItem = 0
    private var visibleItemCount = 0
    private var totalItemCount = 0
    private var isLoading = true

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)

        val gridLayoutManager = (layoutManager as GridLayoutManager)
        visibleItemCount = recyclerView.childCount
        totalItemCount = gridLayoutManager.itemCount
        firstVisibleItem = gridLayoutManager.findFirstVisibleItemPosition()

        if (isLoading) {
            if (totalItemCount > previousTotalItemCount) {
                isLoading = false
                previousTotalItemCount = totalItemCount
            }
        }

        if (isLoading.not() && totalItemCount - visibleItemCount <= firstVisibleItem + visibleThreshold) {
            currentPage++
            onLoadMore(currentPage)
            isLoading = true
        }
    }

    fun resetState() {
        currentPage = 1
        previousTotalItemCount = 0
        isLoading = true
    }

    abstract fun onLoadMore(page: Int)
}