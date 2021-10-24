package com.nunar.nun_ar_android_v1.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.nunar.nun_ar_android_v1.R
import com.nunar.nun_ar_android_v1.adapter.callback.PostResponseDiffUtilCallback
import com.nunar.nun_ar_android_v1.databinding.ItemSearchResultBinding
import com.nunar.nun_ar_android_v1.model.response.PostResponse
import com.nunar.nun_ar_android_v1.widget.SingleLiveEvent

class SearchResultAdapter : ListAdapter<PostResponse, SearchResultAdapter.SearchResultViewHolder>(
    PostResponseDiffUtilCallback) {

    companion object {
        val onClick = SingleLiveEvent<Int>()
    }

    class SearchResultViewHolder(private val binding: ItemSearchResultBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: PostResponse) {
            binding.tvTitle.text = item.title
            binding.tvTag.text = item.tag
            binding.tvWrittenDate.text = item.writer
            binding.btnBookmark.setOnClickListener {

            }

            binding.layout.setOnClickListener { onClick.value = item.postIdx }

            Glide.with(binding.root.context)
                .load("https://nun-ar.com/image/${item.thumbnail}")
                .into(binding.ivThumbnail)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchResultViewHolder {
        return SearchResultViewHolder(DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_search_result,
            parent, false
        ))
    }

    override fun onBindViewHolder(holder: SearchResultViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}