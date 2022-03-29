package com.nunar.nun_ar_android_v1.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.nunar.nun_ar_android_v1.R
import com.nunar.nun_ar_android_v1.adapter.callback.PostResponseDiffUtilCallback
import com.nunar.nun_ar_android_v1.databinding.ItemPopularPostBinding
import com.nunar.nun_ar_android_v1.model.Server.DOMAIN
import com.nunar.nun_ar_android_v1.model.Server.DOMAIN_FILE
import com.nunar.nun_ar_android_v1.model.response.PostResponse
import com.nunar.nun_ar_android_v1.widget.SingleLiveEvent

class PopularPostAdapter :
    ListAdapter<PostResponse, PopularPostAdapter.PopularPostViewHolder>(PostResponseDiffUtilCallback) {

    companion object {
        val onClick = SingleLiveEvent<Int>()
    }

    class PopularPostViewHolder(private val binding: ItemPopularPostBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(post: PostResponse) {

            binding.tvTitle.text = post.title
            binding.tvTag.text = post.tag

            binding.cvThumbnail.setOnClickListener {
                onClick.value = post.postIdx
            }

            Glide.with(binding.root.context)
                .load("${DOMAIN_FILE}image/${post.thumbnail}")
                .into(binding.ivThumbnail)

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PopularPostViewHolder {
        return PopularPostViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_popular_post,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: PopularPostViewHolder, position: Int) {
        holder.bind(getItem(position))
    }


}