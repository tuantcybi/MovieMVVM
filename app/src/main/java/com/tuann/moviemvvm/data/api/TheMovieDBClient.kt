package com.tuann.moviemvvm.data.api


import android.util.Log.VERBOSE
import com.ihsanbal.logging.Level
import com.ihsanbal.logging.LoggingInterceptor
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.internal.platform.android.AndroidLogHandler.setLevel
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

const val API_KEY = "4ad57cb3bf89bbb6b5941e73607715fa"
const val BASE_URL = "https://api.themoviedb.org/3/ "
const val POSTER_BASE_URL = "https://image.tmdb.org/t/p/w220_and_h330_face"
const val FIRST_PAGE = 1
const val POST_PER_PAGE = 20


object TheMovieDBClient {
    fun getClient(): TheMovieDBInterface {
        val requestInterceptor = Interceptor { chain ->
            val url = chain.request()
                .url
                .newBuilder()
                .addQueryParameter("api_key", API_KEY)
                .build()
            val request = chain.request()
                .newBuilder()
                .url(url)
                .build()
            return@Interceptor chain.proceed(request)
        }
//        val okHttpClient: OkHttpClient = OkHttpClient.Builder()
//            .addInterceptor(requestInterceptor)
//            .connectTimeout(60, TimeUnit.SECONDS)
//            .build()
//        val logging = HttpLoggingInterceptor()
//        logging.setLevel(HttpLoggingInterceptor.Level.BASIC)

        val loggingInterceptor = LoggingInterceptor.Builder()
        .setLevel(Level.BASIC)
            .log(VERBOSE)
            .build()

        val client = OkHttpClient.Builder()
            .addInterceptor(requestInterceptor)
            .addInterceptor(loggingInterceptor)
            .build()
        return Retrofit.Builder()
            .client(client)
            .baseUrl(BASE_URL)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(TheMovieDBInterface::class.java)
    }
}