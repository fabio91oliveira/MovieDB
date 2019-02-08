package oliveira.fabio.moviedbapp.util

import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.view.View
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import android.widget.TextView
import androidx.core.content.ContextCompat
import oliveira.fabio.moviedbapp.R
import java.text.SimpleDateFormat
import java.util.*

private object Constants {
    const val EMPTY_YEAR = "NO YEAR WERE LOADED"
}

fun String.getYearFromString(): String {
    return if (this.isEmpty()) {
        Constants.EMPTY_YEAR
    } else {
        val date = SimpleDateFormat("yyyy-MM-dd").parse(this)
        val calendar = Calendar.getInstance()
        calendar.time = date

        calendar.get(Calendar.YEAR).toString()
    }
}

fun TextView.changeStyle() {
    val spannable = SpannableString(text)
    spannable.setSpan(
        ForegroundColorSpan(ContextCompat.getColor(context, R.color.colorRed)),
        0, text.length,
        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
    )
    spannable.setSpan(StyleSpan(Typeface.BOLD), 0, text.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

    text = spannable
}

fun View.doRotateAnimation() {
    val rotate = RotateAnimation(
        0f, 360f,
        Animation.RELATIVE_TO_SELF, 0.5f,
        Animation.RELATIVE_TO_SELF, 0.5f
    )

    rotate.duration = 900
    rotate.repeatCount = Animation.INFINITE
    startAnimation(rotate)
}