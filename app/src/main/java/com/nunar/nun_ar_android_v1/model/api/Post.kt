package com.nunar.nun_ar_android_v1.model.api

import com.nunar.nun_ar_android_v1.model.request.PostRequest
import com.nunar.nun_ar_android_v1.model.response.BaseResponse
import com.nunar.nun_ar_android_v1.model.response.PostResponse
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface Post {

    @GET("post")
    fun getAllPost(): Single<BaseResponse<List<PostResponse>>>

    @GET("post/popular")
    fun getPopularPost(): Single<BaseResponse<List<PostResponse>>>

    @GET("post/{postIdx}")
    fun getPostByIdx(@Path("postIdx") postIdx: Int): Single<BaseResponse<PostResponse>>

    @GET("post/search")
    fun getSearchResult(@Query("searchWord") searchWord: String): Single<BaseResponse<List<PostResponse>>>

    @GET("post/user/{userIdx}")
    fun getPostByUserIdx(@Path("userIdx") userIdx: Int): Single<BaseResponse<List<PostResponse>>>

    @POST("post")
    fun writePost(@Body request: PostRequest): Single<BaseResponse<Int>>
  
}