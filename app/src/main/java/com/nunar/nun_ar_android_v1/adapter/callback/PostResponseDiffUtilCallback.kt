package com.nunar.nun_ar_android_v1.adapter.callback

import androidx.recyclerview.widget.DiffUtil
import com.nunar.nun_ar_android_v1.model.response.PostResponse

object PostResponseDiffUtilCallback : DiffUtil.ItemCallback<PostResponse>() {
    override fun areItemsTheSame(oldItem: PostResponse, newItem: PostResponse) =
        oldItem.postIdx == newItem.postIdx

    override fun areContentsTheSame(oldItem: PostResponse, newItem: PostResponse) =
        oldItem == newItem

}