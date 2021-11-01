package com.nunar.nun_ar_android_v1.model.api

import com.nunar.nun_ar_android_v1.model.request.ModifyUserInfoRequest
import com.nunar.nun_ar_android_v1.model.response.BaseResponse
import com.nunar.nun_ar_android_v1.model.response.UserResponse
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH

interface User {
    @GET("user/my")
    fun getMyInfo(): Single<BaseResponse<UserResponse>>

    @PATCH("user")
    fun modifyUserInfo(@Body modifyUserInfoRequest: ModifyUserInfoRequest): Single<BaseResponse<Unit>>
}