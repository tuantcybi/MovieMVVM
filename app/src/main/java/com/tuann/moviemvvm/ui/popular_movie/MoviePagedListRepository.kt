package com.tuann.moviemvvm.ui.popular_movie

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.tuann.moviemvvm.data.api.POST_PER_PAGE
import com.tuann.moviemvvm.data.api.TheMovieDBInterface
import com.tuann.moviemvvm.data.respository.MovieDataSource
import com.tuann.moviemvvm.data.respository.MovieDataSourceFactory
import com.tuann.moviemvvm.data.respository.NetworkState
import com.tuann.moviemvvm.data.vo.Movie
import io.reactivex.disposables.CompositeDisposable

@Suppress("DEPRECATION")
class MoviePagedListRepository (private val apiService: TheMovieDBInterface) {
    lateinit var moviePagedList: LiveData<PagedList<Movie>>
    lateinit var movieDataSourceFactory: MovieDataSourceFactory

    fun fetchLiveMoviePagedList (compositeDisposable: CompositeDisposable) : LiveData<PagedList<Movie>>{
        movieDataSourceFactory = MovieDataSourceFactory(apiService, compositeDisposable)
        val config : PagedList.Config = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPageSize(POST_PER_PAGE)
            .build()
        moviePagedList = LivePagedListBuilder(movieDataSourceFactory, config).build()

        return moviePagedList

    }
    fun getNetworkState(): LiveData<NetworkState>{
        return Transformations.switchMap<MovieDataSource, NetworkState>(
            movieDataSourceFactory.moviesLiveDataSource, MovieDataSource::networkState
        )

    }
}