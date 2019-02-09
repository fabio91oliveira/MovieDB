package oliveira.fabio.moviedbapp.feature.moviedetail.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.activity_movie_detail.*
import oliveira.fabio.moviedbapp.R
import oliveira.fabio.moviedbapp.feature.moviedetail.viewmodel.MovieDetailViewModel
import oliveira.fabio.moviedbapp.model.MovieDetailResponse
import oliveira.fabio.moviedbapp.model.Response
import oliveira.fabio.moviedbapp.util.BACKIMAGE_HEIGHT
import oliveira.fabio.moviedbapp.util.BACKIMAGE_WIDTH
import oliveira.fabio.moviedbapp.util.enums.ImageQualityEnum
import oliveira.fabio.moviedbapp.util.extensions.doRotateAnimation
import oliveira.fabio.moviedbapp.util.extensions.loadImageByGlide
import org.koin.android.viewmodel.ext.android.viewModel


class MovieDetailActivity : AppCompatActivity() {

    companion object {
        const val MOVIE_ID = "MOVIE_ID"
    }

    private val idMovie by lazy { intent?.extras?.getInt(MOVIE_ID) }
    private val viewModel: MovieDetailViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_detail)
        if (savedInstanceState == null) {
            init()
        } else {
            initLiveData()
            initClickListeners()
            setupToolbar()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.onDestroy()
    }

    private fun init() {
        hideErrorMessage()
        hideContent()
        showLoading()
        initLiveData()
        initClickListeners()
        setupToolbar()
        getMovieById()
    }

    private fun setupToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        toolbar.setNavigationOnClickListener { finish() }
    }


    private fun getMovieById() = idMovie?.run { viewModel.getMovieDetail(this) }

    private fun initClickListeners() {
        imgError.setOnClickListener {
            hideErrorMessage()
            hideContent()
            showLoading()
            getMovieById()
        }
        txtErrorMessage.setOnClickListener {
            hideErrorMessage()
            hideContent()
            showLoading()
            getMovieById() }
    }

    private fun initLiveData() {
        viewModel.movieDetailMutableLiveData.observe(this, Observer {
            when (it.statusEnum) {
                Response.StatusEnum.SUCCESS -> {
                    it?.data?.run {
                        setValues(this)
                        showContent()
                        hideErrorMessage()
                    }
                }
                Response.StatusEnum.ERROR -> {
                    showErrorMessage()
                    hideContent()
                }
            }
            hideLoading()
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
        var image = Any()

        if (urlImage != null) {
            image = ImageQualityEnum.ORIGINAL.getUrlWithQualityPrefix() + urlImage
        } else {
            ContextCompat.getDrawable(this, R.drawable.no_image_landscape)?.let { image = it }
        }
        imgBack.loadImageByGlide(image, BACKIMAGE_WIDTH, BACKIMAGE_HEIGHT)
    }

    private fun share(movieName: String) {
        val shareBody =
            resources.getString(oliveira.fabio.moviedbapp.R.string.movie_detail_share_message_content, movieName)
        val sharingIntent = Intent(android.content.Intent.ACTION_SEND)
        sharingIntent.type = "text/plain"
        sharingIntent.putExtra(
            android.content.Intent.EXTRA_SUBJECT,
            resources.getString(R.string.movie_detail_share_message_title)
        )
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody)
        startActivity(
            Intent.createChooser(
                sharingIntent,
                resources.getString(R.string.movie_detail_share_message_title)
            )
        )
    }

    private fun showErrorMessage() {
        errorGroup.visibility = VISIBLE
    }

    private fun showLoading() {
        loading.doRotateAnimation()
        loading.visibility = VISIBLE
    }

    private fun showContent() {
        group.visibility = VISIBLE
    }

    private fun hideErrorMessage() {
        errorGroup.visibility = GONE
    }

    private fun hideLoading() {
        loading.clearAnimation()
        loading.visibility = GONE
    }

    private fun hideContent() {
        group.visibility = GONE
    }
}