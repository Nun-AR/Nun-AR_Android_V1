package com.nunar.nun_ar_android_v1.model.api

import com.nunar.nun_ar_android_v1.model.response.BaseResponse
import com.nunar.nun_ar_android_v1.model.response.PostResponse
import io.reactivex.Single
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface Bookmark {

    @GET("bookmark/my")
    fun getMyBookmark(): Single<BaseResponse<List<PostResponse>>>

    @GET("bookmark/user/{userIdx}")
    fun getBookmarkPostByUserIdx(@Path("userIdx") userIdx: Int): Single<BaseResponse<List<PostResponse>>>

    @POST("bookmark/{postIdx}")
    fun postBookmark(@Path("postIdx") postIdx: Int): Single<BaseResponse<Unit>>

    @DELETE("bookmark/{postIdx}")
    fun deleteBookmark(@Path("postIdx") postIdx: Int): Single<BaseResponse<Unit>>

}