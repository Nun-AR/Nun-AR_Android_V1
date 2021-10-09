package com.nunar.nun_ar_android_v1.model

import com.nunar.nun_ar_android_v1.model.api.Auth
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

object Server {

    private val AWS_URL = "http://3.37.250.4:8080/v1/"

    private val interceptor = HttpLoggingInterceptor()
    private val client = OkHttpClient.Builder()
        .addInterceptor(interceptor)
        .build()

    private val server = Retrofit.Builder()
        .baseUrl(AWS_URL)
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build()

    val authApi: Auth = server.create(Auth::class.java)
}