package oliveira.fabio.moviedbapp.util

import oliveira.fabio.moviedbapp.util.extensions.dpToPx

// URLS
const val BASE_URL_REQUEST = "https://api.themoviedb.org/3/"
const val API_KEY = "83d01f18538cb7a275147492f84c3698"
const val BASE_URL_IMAGES = "http://image.tmdb.org/t/p/"

// RETURNS
const val EMPTY_YEAR = "NO YEAR WERE LOADED"

// FORMATS
const val DEFAULT_DATE_FORMAT = "yyyy-MM-dd"
const val DURATION_FORMAT = "%dh%02dm"

// SIZES
val POSTER_IMAGE_SIZE = 180f.dpToPx().toInt()
val BACKIMAGE_WIDTH = 200f.dpToPx().toInt()
val BACKIMAGE_HEIGHT = 260f.dpToPx().toInt()