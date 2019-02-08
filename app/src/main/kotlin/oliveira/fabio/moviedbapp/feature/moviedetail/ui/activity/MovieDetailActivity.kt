package oliveira.fabio.moviedbapp.feature.moviedetail.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.activity_movie_detail.*
import oliveira.fabio.moviedbapp.R
import oliveira.fabio.moviedbapp.feature.moviedetail.viewmodel.MovieDetailViewModel
import oliveira.fabio.moviedbapp.model.MovieDetailResponse
import oliveira.fabio.moviedbapp.model.Response
import oliveira.fabio.moviedbapp.util.BASE_URL_IMAGES
import oliveira.fabio.moviedbapp.util.doRotateAnimation
import org.koin.android.viewmodel.ext.android.viewModel


class MovieDetailActivity : AppCompatActivity() {

    companion object {
        const val MOVIE_ID = "MOVIE_ID"
    }

    private val circularProgressDrawable by lazy { CircularProgressDrawable(this) }
    private val idMovie by lazy { intent?.extras?.getInt(MOVIE_ID) }
    private val viewModel: MovieDetailViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_detail)
        init()

        if (savedInstanceState == null) {
            init()
        } else {
            initLiveData()
            setupToolbar()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.onDestroy()
    }

    private fun init() {
        initLoadingGlide()
        showLoading()
        getMovieById()
        initLiveData()
        setupToolbar()
    }

    private fun initLoadingGlide() {
        circularProgressDrawable.strokeWidth = 5f
        circularProgressDrawable.centerRadius = 30f
        circularProgressDrawable.start()
    }

    private fun setupToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        toolbar.setNavigationOnClickListener { finish() }
    }


    private fun getMovieById() = idMovie?.run { viewModel.getMovieDetail(this) }

    private fun initLiveData() {
        viewModel.movieDetailMutableLiveData.observe(this, Observer {
            when (it.statusEnum) {
                Response.StatusEnum.SUCCESS -> {
                    it?.data?.run {
                        setValues(this)
                    }
                }
                Response.StatusEnum.ERROR -> {
                    val a = ""
                }
            }
        })
    }

    private fun setValues(movieDetailResponse: MovieDetailResponse) {
        loadImage(movieDetailResponse.backdropPath)
        txtTitle.text = movieDetailResponse.originalTitle
        txtDuration.text = movieDetailResponse.getDurationFormatted()
        txtBudget.text = movieDetailResponse.getBudgetFormatted()
        txtOverview.text = movieDetailResponse.overview
        btnShare.setOnClickListener { share(movieDetailResponse.originalTitle) }
        hideLoading()
    }

    private fun loadImage(urlImage: String) {
        Glide.with(this).load(BASE_URL_IMAGES + urlImage)
            .apply(
                RequestOptions().placeholder(circularProgressDrawable).error(R.color.colorPrimaryDark).diskCacheStrategy(
                    DiskCacheStrategy.ALL
                )
            )
            .transition(
                DrawableTransitionOptions.withCrossFade()
            ).into(imgBack)
    }

    private fun share(movieName: String) {
        val shareBody =
            resources.getString(oliveira.fabio.moviedbapp.R.string.movie_detail_share_message_content, movieName)
        val sharingIntent = Intent(android.content.Intent.ACTION_SEND)
        sharingIntent.type = "text/plain"
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject Here")
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody)
        startActivity(
            Intent.createChooser(
                sharingIntent,
                resources.getString(R.string.movie_detail_share_message_title)
            )
        )
    }

    private fun showLoading() {
        loading.doRotateAnimation()
        loading.visibility = VISIBLE
        group.visibility = GONE
    }

    private fun hideLoading() {
        loading.clearAnimation()
        loading.visibility = GONE
        group.visibility = VISIBLE
    }
}