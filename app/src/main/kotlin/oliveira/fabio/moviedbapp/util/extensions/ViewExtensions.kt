package oliveira.fabio.moviedbapp.util.extensions

import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.view.View
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import oliveira.fabio.moviedbapp.R

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

fun AppCompatImageView.loadImageByGlide(image: Any, width: Int, height: Int) {
    val circularProgressDrawable = CircularProgressDrawable(context)
    circularProgressDrawable.strokeWidth = 5f
    circularProgressDrawable.centerRadius = 30f
    circularProgressDrawable.start()

    Glide.with(context).load(image)
        .apply(
            RequestOptions().override(
                width,
                height
            ).priority(Priority.IMMEDIATE).placeholder(circularProgressDrawable).error(R.color.colorAccent).diskCacheStrategy(
                DiskCacheStrategy.ALL
            )
        )
        .transition(
            DrawableTransitionOptions.withCrossFade()
        ).into(this)
}