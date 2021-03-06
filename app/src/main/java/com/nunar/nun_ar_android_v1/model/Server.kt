package com.nunar.nun_ar_android_v1.model

import androidx.datastore.preferences.core.stringPreferencesKey
import com.nunar.nun_ar_android_v1.NunARApplication
import com.nunar.nun_ar_android_v1.model.api.*
import com.nunar.nun_ar_android_v1.utils.tokenDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

object Server {

    val DOMAIN = "http://13.125.197.20:8080/v1/"
    val DOMAIN_FILE = "http://13.125.197.20:8080/"

    class TokenInterceptor : Interceptor {
        private val tokenKey = stringPreferencesKey("token")
        override fun intercept(chain: Interceptor.Chain): Response {
            val token =
                runBlocking { NunARApplication.context?.tokenDataStore?.data?.first() }?.get(
                    tokenKey)
            val request =
                chain.request().newBuilder().addHeader("Authorization", "Bearer $token").build()
            val response = chain.proceed(request)
            return if (response.code == 403) {
                chain.proceed(chain.request())
            } else {
                response
            }
        }
    }

    private val interceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    private val client = OkHttpClient.Builder()
        .addInterceptor(interceptor)
        .addInterceptor(TokenInterceptor())
        .build()

    private val server = Retrofit.Builder()
        .baseUrl(DOMAIN)
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build()

    val authApi: Auth = server.create(Auth::class.java)
    val postApi: Post = server.create(Post::class.java)
    val uploadApi: Upload = server.create(Upload::class.java)
    val userApi: User = server.create(User::class.java)
    val bookmarkApi: Bookmark = server.create(Bookmark::class.java)
}