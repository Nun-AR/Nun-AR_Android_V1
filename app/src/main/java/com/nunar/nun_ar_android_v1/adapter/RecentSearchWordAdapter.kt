package com.nunar.nun_ar_android_v1.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ListView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.nunar.nun_ar_android_v1.R
import com.nunar.nun_ar_android_v1.adapter.RecentSearchWordAdapter.*
import com.nunar.nun_ar_android_v1.databinding.ItemRecentSearchWordBinding
import com.nunar.nun_ar_android_v1.widget.SingleLiveEvent

class RecentSearchWordAdapter :
    ListAdapter<String, RecentSearchWordViewHolder>(object : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String) = oldItem == newItem
        override fun areContentsTheSame(oldItem: String, newItem: String) = oldItem == newItem
    }) {

    companion object {
        val onClick = SingleLiveEvent<String>()
    }

    class RecentSearchWordViewHolder(private val binding: ItemRecentSearchWordBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(searchWord: String) {
            binding.tvSearchWord.text = searchWord
            binding.layout.setOnClickListener { onClick.value = searchWord }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecentSearchWordViewHolder {
        return RecentSearchWordViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_recent_search_word,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecentSearchWordViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

}