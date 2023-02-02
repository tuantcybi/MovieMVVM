package com.tuann.moviemvvm.ui.popular_movie


import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tuann.moviemvvm.data.api.POSTER_BASE_URL
import com.tuann.moviemvvm.data.respository.NetworkState
import com.tuann.moviemvvm.data.vo.Movie

import com.tuann.moviemvvm.databinding.MovieListItemBinding
import com.tuann.moviemvvm.databinding.NetworkStateItemBinding
import com.tuann.moviemvvm.ui.movie_details.SingleMovie


class PopularMoviePagedListAdapter( val  context: Context) :
    PagedListAdapter<Movie, RecyclerView.ViewHolder>(MovieDiffCallback()) {

    val MOVIE_VIEW_TYPE = 1
    val NETWORK_VIEW_TYPE = 2
    private var networkState: NetworkState? = null

    // 1. Trả lời cho câu hỏi - Có bao nhiêu item cần hiển thị
    override fun getItemCount(): Int {
        return super.getItemCount() + if (hasExtraRow()) 1 else 0
    }

    private fun hasExtraRow(): Boolean {
        return networkState != null && networkState != NetworkState.LOADED
    }

    // 2. Trả lời cho câu hỏi - Tại vị trí X thì item hiển thị là type nào
    override fun getItemViewType(position: Int): Int {
        return if (hasExtraRow() && position == itemCount - 1) {
            NETWORK_VIEW_TYPE
        } else {
            MOVIE_VIEW_TYPE
        }
    }

    // 3. Trả lời cho câu hỏi - Với viewType này, bạn muốn hiển thị item như thế nào
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater: LayoutInflater = LayoutInflater.from(parent.context)
        if (viewType == MOVIE_VIEW_TYPE) {
            val binding = MovieListItemBinding.inflate(layoutInflater, parent, false)
            return MovieItemViewHolder(binding)
        } else {
            val binding = NetworkStateItemBinding.inflate(layoutInflater, parent, false)
            return NetworkStateItemViewHolder(binding)
        }
    }

    // 4. Trả lời cho câu hỏi - Sau khi tạo UI cho item, bạn muốn liên kết dữ liệu của item với UI như thế nào
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is MovieItemViewHolder -> {
                val movie = currentList?.get(index = position)

                holder.bind(movie = movie, context = context)
            }
        }
    }


    class MovieDiffCallback : DiffUtil.ItemCallback<Movie>() {
        override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem == newItem
        }
    }

    class MovieItemViewHolder(private val  binding: MovieListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(movie: Movie?, context: Context) {
            binding.cvIvMovieTitle.text = movie?.title
            binding.cvIvMovieReleaseDate.text = movie?.releaseDate
            val moviePosterURL: String = POSTER_BASE_URL + movie?.posterPath
            Glide.with(itemView.context)
                .load(moviePosterURL)
                .into(binding.cvIvMoviePoster);
            itemView.setOnClickListener {
                val intent = Intent(context, SingleMovie::class.java)
                intent.putExtra("id", movie?.id)
                context.startActivity(intent)
            }
        }
    }

    class NetworkStateItemViewHolder(private val binding: NetworkStateItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(networkState: NetworkState?) {
            if (networkState != null && networkState == NetworkState.LOADING) {
                binding.progressBarItem.visibility = View.VISIBLE;
            } else {
                binding.progressBarItem.visibility = View.GONE;
            }
            if (networkState != null && networkState == NetworkState.ERROR) {
                binding.errorMsgItem.visibility = View.VISIBLE;
                binding.errorMsgItem.text = networkState.msg;
            } else if (networkState != null && networkState == NetworkState.ENDOFLIST) {
                binding.errorMsgItem.visibility = View.VISIBLE;
                binding.errorMsgItem.text = networkState.msg;
            } else {
                binding.errorMsgItem.visibility = View.GONE;
            }
        }
    }

    fun setNetworkState(newNetworkState: NetworkState) {
        val previousState: NetworkState? = this.networkState
        val hadExtraRow: Boolean = hasExtraRow()
        this.networkState = newNetworkState
        val hasExtraRow: Boolean = hasExtraRow()
        if (hadExtraRow != hasExtraRow) {
            if (hasExtraRow) {
                notifyItemRemoved(super.getItemCount())
            } else {
                notifyItemInserted(super.getItemCount())
            }
        } else if (hasExtraRow && previousState != newNetworkState) {
            notifyItemChanged(itemCount - 1)
        }

    }

}