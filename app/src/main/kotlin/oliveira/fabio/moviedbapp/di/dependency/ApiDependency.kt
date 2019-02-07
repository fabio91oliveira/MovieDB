package oliveira.fabio.moviedbapp.di.dependency

import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import oliveira.fabio.moviedbapp.network.MovieApi
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.util.concurrent.TimeUnit


object ApiDependency {
    private const val CONNECT_TIME_OUT: Long = 1
    private const val READ_TIME_OUT: Long = 2
    private const val WRITE_TIME_OUT: Long = 3
    private const val BASE_URL = "https://api.themoviedb.org/3/"
    private const val KEY = "83d01f18538cb7a275147492f84c3698"

    private fun provideGson(): Gson {
        var gsonBuilder = GsonBuilder()
        gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
        return gsonBuilder.create()
    }

    private fun provideOkHttpClient(): OkHttpClient {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BASIC
        return OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .addInterceptor(object : Interceptor {
                @Throws(IOException::class)
                override fun intercept(chain: Interceptor.Chain): Response {
                    val original = chain.request()
                    val originalHttpUrl = original.url()

                    val url = originalHttpUrl.newBuilder()
                        .addQueryParameter("format", "json")
                        .addQueryParameter("api_key", KEY)
                        .build()

                    val requestBuilder = original.newBuilder()
                        .url(url)

                    val request = requestBuilder.build()
                    return chain.proceed(request)
                }
            })
            .connectTimeout(CONNECT_TIME_OUT, TimeUnit.SECONDS)
            .readTimeout(READ_TIME_OUT, TimeUnit.SECONDS)
            .writeTimeout(WRITE_TIME_OUT, TimeUnit.SECONDS)
            .build()
    }

    fun provideMovieApi(): MovieApi {
        val retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(ApiDependency.provideGson()))
            .client(ApiDependency.provideOkHttpClient())
            .baseUrl(ApiDependency.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
        return retrofit.create(MovieApi::class.java)
    }
}