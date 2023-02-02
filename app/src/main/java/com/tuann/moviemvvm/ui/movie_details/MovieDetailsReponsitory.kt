package com.tuann.moviemvvm.ui.movie_details

import androidx.lifecycle.LiveData
import com.tuann.moviemvvm.data.api.TheMovieDBInterface
import com.tuann.moviemvvm.data.respository.MovieDetailsNetworkDataSource
import com.tuann.moviemvvm.data.respository.NetworkState
import com.tuann.moviemvvm.data.vo.MovieDetails
import io.reactivex.disposables.CompositeDisposable


class MovieDetailsReponsitory(private val apiService: TheMovieDBInterface) {
    lateinit var movieDetailsNetworkDataSource: MovieDetailsNetworkDataSource
    fun fetchSingleMovieDetails(
        compositeDisposable: CompositeDisposable,
        movieId: Int
    ): LiveData<MovieDetails> {
        movieDetailsNetworkDataSource = MovieDetailsNetworkDataSource(apiService,compositeDisposable)
        movieDetailsNetworkDataSource.fetchMovieDetails(movieId)
        return movieDetailsNetworkDataSource.downloadedMovieResponse
    }
    fun getMovieDetailsNetworkState(): LiveData<NetworkState>{
        return movieDetailsNetworkDataSource.networkState
    }
}