package com.nunar.nun_ar_android_v1.model.api

import com.nunar.nun_ar_android_v1.model.response.BaseResponse
import com.nunar.nun_ar_android_v1.model.response.PostResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path

interface Post {

    @GET("post")
    fun getAllPost(): Single<BaseResponse<List<PostResponse>>>

    @GET("post/popular")
    fun getPopularPost(): Single<BaseResponse<List<PostResponse>>>

    @GET("post/{postIdx}")
    fun getPostByIdx(@Path("postIdx") postIdx: Int): Single<BaseResponse<List<PostResponse>>>

}