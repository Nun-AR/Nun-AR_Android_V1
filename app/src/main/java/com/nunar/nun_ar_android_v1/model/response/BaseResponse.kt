package com.nunar.nun_ar_android_v1.model.response

data class BaseResponse<T>(
    val code: Int,
    val message: String,
    val data: T
)