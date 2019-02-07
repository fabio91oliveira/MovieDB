package oliveira.fabio.moviedbapp

import android.app.Application
import oliveira.fabio.moviedbapp.di.remoteModule
import oliveira.fabio.moviedbapp.di.repositoryModule
import oliveira.fabio.moviedbapp.di.viewModelModule
import org.koin.android.ext.android.startKoin

class MovieDbApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin(this, listOf(remoteModule, repositoryModule, viewModelModule))
    }
}