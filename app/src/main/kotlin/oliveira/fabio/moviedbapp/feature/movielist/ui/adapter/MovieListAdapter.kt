package oliveira.fabio.moviedbapp.feature.movielist.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_movie.*
import oliveira.fabio.moviedbapp.R
import oliveira.fabio.moviedbapp.model.MovieResponse


class MovieListAdapter(val context: Context) :
    RecyclerView.Adapter<MovieListAdapter.CustomViewHolder>() {

    private var results: MutableList<MovieResponse.Result> = mutableListOf()

    fun addResult(result: List<MovieResponse.Result>) = results.addAll(result)
    override fun getItemCount() = results.size
    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) = holder.bind(results[position])

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        CustomViewHolder(
            LayoutInflater.from(parent.context).inflate(
                oliveira.fabio.moviedbapp.R.layout.item_movie,
                parent,
                false
            )
            , context
        )


    class CustomViewHolder(override val containerView: View, private val context: Context) :
        RecyclerView.ViewHolder(containerView), LayoutContainer {
        companion object {
            private const val BASE_URL = "http://image.tmdb.org/t/p/w185"
        }

        fun bind(result: MovieResponse.Result) {
            loadImage(result)
            txtTitle.text = result.originalTitle
            txtDate.text = result.releaseDate
        }

        private fun loadImage(result: MovieResponse.Result) {
            Glide.with(context).load(BASE_URL + result.posterPath)
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
    }
}