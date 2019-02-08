package oliveira.fabio.moviedbapp.model

import java.text.NumberFormat
import java.util.*
import java.util.concurrent.TimeUnit

data class MovieDetailResponse(
        val adult: Boolean = false,
        val backdropPath: String = "",
        val belongsToCollection: Any? = Any(),
        val budget: Int = 0,
        val genres: List<Genre> = listOf(),
        val homepage: String = "",
        val id: Int = 0,
        val imdbId: String = "",
        val originalLanguage: String = "",
        val originalTitle: String = "",
        val overview: String = "",
        val popularity: Double = 0.0,
        val posterPath: String = "",
        val productionCompanies: List<ProductionCompany> = listOf(),
        val productionCountries: List<ProductionCountry> = listOf(),
        val releaseDate: String = "",
        val revenue: Int = 0,
        val runtime: Int = 0,
        val spokenLanguages: List<SpokenLanguage> = listOf(),
        val status: String = "",
        val tagline: String = "",
        val title: String = "",
        val video: Boolean = false,
        val voteAverage: Double = 0.0,
        val voteCount: Int = 0
) {

    companion object {
        private const val DURATION_FORMAT = "%dh%02dm"
    }

    data class ProductionCompany(
            val id: Int = 0,
            val logoPath: Any? = Any(),
            val name: String = "",
            val originCountry: String = ""
    )

    data class ProductionCountry(
            val iso31661: String = "",
            val name: String = ""
    )

    data class Genre(
            val id: Int = 0,
            val name: String = ""
    )

    data class SpokenLanguage(
            val iso6391: String = "",
            val name: String = ""
    )

    fun getDurationFormatted(): String {
        val hours = TimeUnit.MINUTES.toHours(runtime.toLong())
        val minutes = runtime - TimeUnit.HOURS.toMinutes(hours)

        return String.format(DURATION_FORMAT, hours, minutes)
    }

    fun getBudgetFormatted(): String {
        val numberFormat = NumberFormat.getCurrencyInstance(Locale.US)
        return numberFormat.format(budget / 100.0)
    }
}