package oliveira.fabio.moviedbapp.util.extensions

import android.content.res.Resources
import oliveira.fabio.moviedbapp.util.DEFAULT_DATE_FORMAT
import oliveira.fabio.moviedbapp.util.EMPTY_YEAR
import java.text.SimpleDateFormat
import java.util.*

fun String.getYearFromString(): String {
    return if (this.isEmpty()) {
        EMPTY_YEAR
    } else {
        val date = SimpleDateFormat(DEFAULT_DATE_FORMAT).parse(this)
        val calendar = Calendar.getInstance()
        calendar.time = date

        calendar.get(Calendar.YEAR).toString()
    }
}

fun Float.dpToPx() = this * Resources.getSystem().displayMetrics.density
