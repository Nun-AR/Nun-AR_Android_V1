package com.nunar.nun_ar_android_v1.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.ColorInt
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.nunar.nun_ar_android_v1.R
import com.nunar.nun_ar_android_v1.adapter.SuggestionTagAdapter.*
import com.nunar.nun_ar_android_v1.databinding.ItemSuggestionTagBinding
import com.nunar.nun_ar_android_v1.widget.SingleLiveEvent

class SuggestionTagAdapter :
    ListAdapter<String, SuggestionTagViewHolder>(object : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean = oldItem == newItem

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean =
            oldItem == newItem

    }) {

    companion object {
        val onClick = SingleLiveEvent<String>()
    }

    class SuggestionTagViewHolder(val binding: ItemSuggestionTagBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(tag: String, position: Int) {
            binding.tvTag.text = tag
            binding.cvTag.setOnClickListener { onClick.value = tag }
            if(position % 5 == 0) binding.cvTag.setCardBackgroundColor(Color.parseColor("#F0BECD"))
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SuggestionTagViewHolder {
        return SuggestionTagViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_suggestion_tag,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: SuggestionTagViewHolder, position: Int) {
        holder.bind(getItem(position), position)
    }


}