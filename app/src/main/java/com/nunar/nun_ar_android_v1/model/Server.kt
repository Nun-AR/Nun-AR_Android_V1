package com.nunar.nun_ar_android_v1.model

import androidx.datastore.preferences.core.stringPreferencesKey
import com.nunar.nun_ar_android_v1.NunARApplication
import com.nunar.nun_ar_android_v1.model.api.Auth
import com.nunar.nun_ar_android_v1.model.api.Post
import com.nunar.nun_ar_android_v1.utils.tokenDataStore
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

object Server {

    private val AWS_URL = "http://3.37.250.4:8080/v1/"

    class TokenInterceptor : Interceptor {
        private val tokenKey = stringPreferencesKey("token")
        override fun intercept(chain: Interceptor.Chain): Response {
            val token = runBlocking { NunARApplication.context?.tokenDataStore?.data?.first() }?.get(tokenKey)
            val request =
                chain.request().newBuilder().addHeader("Authorization", "Bearer $token").build()
            return chain.proceed(request)
        }
    }

    private val interceptor = HttpLoggingInterceptor()
    private val client = OkHttpClient.Builder()
        .addInterceptor(interceptor)
        .addInterceptor(TokenInterceptor())
        .build()

    private val server = Retrofit.Builder()
        .baseUrl(AWS_URL)
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build()

    val authApi: Auth = server.create(Auth::class.java)
    val postApi: Post = server.create(Post::class.java)
}