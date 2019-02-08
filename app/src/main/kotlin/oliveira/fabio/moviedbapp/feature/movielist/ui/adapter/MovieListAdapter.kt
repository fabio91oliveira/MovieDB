package oliveira.fabio.moviedbapp.feature.movielist.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_movie.*
import oliveira.fabio.moviedbapp.R
import oliveira.fabio.moviedbapp.model.MoviesResponse
import oliveira.fabio.moviedbapp.util.BASE_URL_IMAGES
import oliveira.fabio.moviedbapp.util.changeStyle
import oliveira.fabio.moviedbapp.util.getYearFromString
import java.util.*


class MovieListAdapter(val onClickMovieListener: OnClickMovieListener) :
    RecyclerView.Adapter<MovieListAdapter.CustomViewHolder>() {

    private var results: MutableList<MoviesResponse.Result> = mutableListOf()

    fun addResult(result: List<MoviesResponse.Result>) = results.addAll(result)
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
        }

        private fun loadImage(result: MoviesResponse.Result) {
            Glide.with(imgPoster.context).load(BASE_URL_IMAGES + result.posterPath)
                .apply(
                    RequestOptions().override(
                        600,
                        800
                    ).placeholder(R.color.colorPrimaryDark).error(R.color.colorAccent).diskCacheStrategy(
                        DiskCacheStrategy.ALL
                    )
                )
                .transition(
                    DrawableTransitionOptions.withCrossFade()
                ).into(imgPoster)
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