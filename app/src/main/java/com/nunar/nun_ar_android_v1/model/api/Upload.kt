package com.nunar.nun_ar_android_v1.model.api

import com.nunar.nun_ar_android_v1.model.response.BaseResponse
import io.reactivex.Single
import okhttp3.MultipartBody
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface Upload {

    @Multipart
    @POST("upload/image")
    fun uploadImage(@Part image: MultipartBody.Part): Single<BaseResponse<String>>

    @Multipart
    @POST("upload/model")
    fun uploadModel(@Part file: MultipartBody.Part): Single<BaseResponse<String>>
}