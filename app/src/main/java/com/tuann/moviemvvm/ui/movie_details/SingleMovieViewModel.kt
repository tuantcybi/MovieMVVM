package com.tuann.moviemvvm.ui.movie_details

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.tuann.moviemvvm.data.respository.NetworkState
import com.tuann.moviemvvm.data.vo.MovieDetails
import io.reactivex.disposables.CompositeDisposable

class SingleMovieViewModel(private val movieReponsititory: MovieDetailsReponsitory, movieId: Int) :
    ViewModel() {
        private val compositeDisposable = CompositeDisposable()
    val movieDetails:LiveData<MovieDetails> by lazy {
        movieReponsititory.fetchSingleMovieDetails(compositeDisposable, movieId)
    }
    val networkState: LiveData<NetworkState> by lazy {
        movieReponsititory.getMovieDetailsNetworkState()
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}