package com.nunar.nun_ar_android_v1.model.request

data class PostRequest(
    val title: String,
    val tag: String,
    val thumbnail: String,
    val fileUrl: String,
    val isPublic: Boolean
)
