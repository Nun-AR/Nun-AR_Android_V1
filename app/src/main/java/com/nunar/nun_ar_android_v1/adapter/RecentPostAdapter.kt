package com.nunar.nun_ar_android_v1.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.nunar.nun_ar_android_v1.R
import com.nunar.nun_ar_android_v1.adapter.callback.PostResponseDiffUtilCallback
import com.nunar.nun_ar_android_v1.databinding.ItemPostBinding
import com.nunar.nun_ar_android_v1.model.response.PostResponse

class RecentPostAdapter :
    ListAdapter<PostResponse, RecentPostAdapter.RecentPostViewHolder>(PostResponseDiffUtilCallback) {

    class RecentPostViewHolder(private val binding: ItemPostBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(post: PostResponse) {
            binding.tvTitle.text = post.title
            binding.tvWrittenDate.text = post.writer
            binding.tvTag.text = post.tag
            binding.btnBookmark.isSelected = post.isBookmarks
            binding.btnBookmark.setOnClickListener {

            }

            Glide.with(binding.root.context)
                .load("https://nun-ar.com/image/${post.thumbnail}")
                .into(binding.ivThumbnail)

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecentPostViewHolder {
        return RecentPostViewHolder(DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_post,
            parent,
            false
        ))
    }

    override fun onBindViewHolder(holder: RecentPostViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

}