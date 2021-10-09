package com.nunar.nun_ar_android_v1.utils

sealed class NetworkStatus<T> {
    class Loading<T> : NetworkStatus<T>()
    data class Success<T>(val data: T) : NetworkStatus<T>()
    data class Error<T>(val data: T? = null, val throwable: Throwable) : NetworkStatus<T>()
}
