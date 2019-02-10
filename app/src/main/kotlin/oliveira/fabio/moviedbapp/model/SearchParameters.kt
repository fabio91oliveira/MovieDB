package oliveira.fabio.moviedbapp.model

data class SearchParameters(
    var results: MutableList<MoviesResponse.Result> = mutableListOf(),
    var sortyBy: String = POPULAR,
    var page: Int = 1
) {

    companion object {
        private const val POPULAR = "popularity.desc"
        private const val RELEASE_DATE = "release_date.desc"
        private const val BUDGET = "revenue.desc"
        private const val VOTE = "vote_average.desc"
    }

    fun addResults(results: List<MoviesResponse.Result>) = this.results.addAll(results)

    fun clearResults() = results.clear()

    fun resetPageCount() {
        page = 1
    }

    fun setPopularSort() {
        sortyBy = POPULAR
    }

    fun setReleaseDateSort() {
        sortyBy = RELEASE_DATE
    }

    fun setBudgetSort() {
        sortyBy = BUDGET
    }

    fun setVoteSort() {
        sortyBy = VOTE
    }

}