package oliveira.fabio.moviedbapp.util.enums

import oliveira.fabio.moviedbapp.util.BASE_URL_IMAGES

enum class ImageQualityEnum(private val prefixQuality: String) {
    PROFILE("h632"),
    BACKDROP("w780"),
    ORIGINAL("original");

    fun getUrlWithQualityPrefix() = BASE_URL_IMAGES + prefixQuality
}