package oliveira.fabio.moviedbapp.feature.movielist.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.extensions.LayoutContainer
import oliveira.fabio.moviedbapp.R
import oliveira.fabio.moviedbapp.model.MovieResponse

class MovieListAdapter(private val results: List<MovieResponse.Result>) :
    RecyclerView.Adapter<MovieListAdapter.CustomViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        CustomViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.activity_movie_list, parent, false))

    override fun getItemCount() = results.size

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) = holder.bind()


    class CustomViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer {
        fun bind() {

        }
    }
}