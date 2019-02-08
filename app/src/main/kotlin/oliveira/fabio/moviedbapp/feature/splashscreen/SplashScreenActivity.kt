package oliveira.fabio.moviedbapp.feature.splashscreen

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_splash_screen.*
import oliveira.fabio.moviedbapp.R
import oliveira.fabio.moviedbapp.feature.movielist.ui.activity.MovieListActivity

class SplashScreenActivity : AppCompatActivity() {

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        val window = window
        val winParams = window.attributes
        winParams.flags = winParams.flags and WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS.inv()
        window.attributes = winParams
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN

        val animation = AnimationUtils.loadAnimation(this, R.anim.hyperspace_jump)
        animation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(anim: Animation) {}
            override fun onAnimationRepeat(anim: Animation) {}
            override fun onAnimationEnd(anim: Animation) {
                proceedToHome()
            }
        })
        imgSplash.startAnimation(animation)
    }

    private fun proceedToHome() {
        Handler().postDelayed({
            val i = Intent(this, MovieListActivity::class.java)
            startActivity(i)
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
            finish()
        }, SPLASH_TIME_OUT.toLong())
    }

    companion object {
        const val SPLASH_TIME_OUT = 1000
    }

}