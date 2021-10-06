package com.nunar.nun_ar_android_v1.model.api

import com.nunar.nun_ar_android_v1.model.request.LoginRequest
import com.nunar.nun_ar_android_v1.model.request.RegisterRequest
import com.nunar.nun_ar_android_v1.model.response.BaseResponse
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.POST

interface Auth {

    @POST("auth/login")
    fun login(@Body loginRequest: LoginRequest): Single<BaseResponse<String>>

    @POST("auth/register")
    fun register(@Body registerRequest: RegisterRequest): Single<BaseResponse<Any>>

}