package com.nunar.nun_ar_android_v1.model.request

data class RegisterRequest(
    val id: String,
    val password: String,
    val name: String,
    val profileUrl: String,
)
