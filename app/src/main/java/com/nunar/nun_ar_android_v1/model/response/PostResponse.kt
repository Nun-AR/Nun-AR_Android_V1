package com.nunar.nun_ar_android_v1.model.response

data class PostResponse(
    val postIdx: Int,
    val userIdx: Int,
    val writer: String,
    val title: String,
    val bookmarks: Int,
    val isBookmarks: Boolean,
    val tag: String,
    val thumbnail: String,
    val fileUrl: String
)