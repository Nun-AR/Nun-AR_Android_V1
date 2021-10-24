package com.nunar.nun_ar_android_v1.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.nunar.nun_ar_android_v1.R
import com.nunar.nun_ar_android_v1.adapter.callback.PostResponseDiffUtilCallback
import com.nunar.nun_ar_android_v1.databinding.ItemSuggestBinding
import com.nunar.nun_ar_android_v1.model.response.PostResponse

class SuggestPostAdapter : ListAdapter<PostResponse, SuggestPostAdapter.SuggestPostViewHolder>(
    PostResponseDiffUtilCallback) {

    class SuggestPostViewHolder(private val binding: ItemSuggestBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(post: PostResponse) {
            binding.suggestTitle.text = post.title
            binding.suggestTag.text = post.tag
            binding.suggestDate.text = post.writer
            binding.suggestBookmark.isSelected = post.isBookmarks
            binding.suggestBookmark.setOnClickListener {

            }

            Glide.with(binding.root.context)
                .load("https://nun-ar.com/image/${post.thumbnail}")
                .into(binding.suggestImage)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SuggestPostViewHolder {
        return SuggestPostViewHolder(DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_suggest,
            parent,
            false
        ))
    }

    override fun onBindViewHolder(holder: SuggestPostViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}