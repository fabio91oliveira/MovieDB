package oliveira.fabio.moviedbapp.feature.movielist.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_movie.*
import oliveira.fabio.moviedbapp.R
import oliveira.fabio.moviedbapp.model.MoviesResponse
import oliveira.fabio.moviedbapp.util.POSTER_IMAGE_SIZE
import oliveira.fabio.moviedbapp.util.enums.ImageQualityEnum
import oliveira.fabio.moviedbapp.util.extensions.changeStyle
import oliveira.fabio.moviedbapp.util.extensions.getYearFromString
import oliveira.fabio.moviedbapp.util.extensions.loadImageByGlide
import java.util.*


class MovieListAdapter(val onClickMovieListener: OnClickMovieListener) :
    RecyclerView.Adapter<MovieListAdapter.CustomViewHolder>() {
    private var results: MutableList<MoviesResponse.Result> = mutableListOf()

    fun addResult(result: List<MoviesResponse.Result>) = results.addAll(result)
    fun clearList() = results.clear()
    override fun getItemCount() = results.size
    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) = holder.bind(results[position])

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        CustomViewHolder(
            LayoutInflater.from(parent.context).inflate(
                oliveira.fabio.moviedbapp.R.layout.item_movie,
                parent,
                false
            )
        )


    inner class CustomViewHolder(override val containerView: View) :
        RecyclerView.ViewHolder(containerView), LayoutContainer {


        fun bind(result: MoviesResponse.Result) {
            loadImage(result)
            txtTitle.text = result.originalTitle
            shouldChangeStyleTextIfIsCurrentYear(txtDate, result.releaseDate.getYearFromString())
            containerView.setOnClickListener {
                onClickMovieListener.setOnClickMovieListener(result.id)
            }
            txtRate.text = result.voteAverage.toString()
        }

        private fun loadImage(result: MoviesResponse.Result) {
            var image = Any()

            if (result.posterPath != null) {
                image = ImageQualityEnum.ORIGINAL.getUrlWithQualityPrefix() + result.posterPath
            } else {
                ContextCompat.getDrawable(containerView.context, R.drawable.no_image)?.let {
                    image = it
                }
            }
            imgPoster.loadImageByGlide(image, POSTER_IMAGE_SIZE, POSTER_IMAGE_SIZE)
        }

        private fun shouldChangeStyleTextIfIsCurrentYear(appCompatTextView: AppCompatTextView, year: String) {
            appCompatTextView.text = year
            if (isCurrentYear(year)) appCompatTextView.changeStyle()
        }

        private fun isCurrentYear(year: String) = year == Calendar.getInstance().get(Calendar.YEAR).toString()
    }

    interface OnClickMovieListener {
        fun setOnClickMovieListener(id: Int)
    }
}