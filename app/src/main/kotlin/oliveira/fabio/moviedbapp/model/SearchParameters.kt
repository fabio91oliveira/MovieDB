package oliveira.fabio.moviedbapp.model

data class SearchParameters(var sortyBy: String = POPULAR, var page: Int = 1) {
    companion object {
        private const val POPULAR = "popularity.desc"
        private const val RELEASE_DATE = "release_date.desc"
        private const val BUDGET = "revenue.desc"
        private const val VOTE = "vote_average.desc"
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