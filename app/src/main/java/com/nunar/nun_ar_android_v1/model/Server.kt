package com.nunar.nun_ar_android_v1.model

import com.nunar.nun_ar_android_v1.model.api.Auth
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

object Server {
    private val interceptor = HttpLoggingInterceptor()
    private val client = OkHttpClient.Builder()
        .addInterceptor(interceptor)
        .build()

    private val server = Retrofit.Builder()
        .baseUrl("http://10.80.163.109:8080/v1/")
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build()

    val authApi = server.create(Auth::class.java)
}