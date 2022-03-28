package com.nunar.nun_ar_android_v1.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.nunar.nun_ar_android_v1.model.Server.authApi
import com.nunar.nun_ar_android_v1.model.Server.uploadApi
import com.nunar.nun_ar_android_v1.model.request.RegisterRequest
import com.nunar.nun_ar_android_v1.utils.NetworkStatus
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

class SignupViewModel : ViewModel() {
    private val disposable = CompositeDisposable()

    val id = MutableLiveData<String>()
    val password = MutableLiveData<String>()
    val name = MutableLiveData<String>()
    val profileFile = MutableLiveData<File>()
    val profileUrl = MutableLiveData<String>()

    private val _imageUpload = MutableLiveData<NetworkStatus<String>>()
    val imageUpload: MutableLiveData<NetworkStatus<String>> get() = _imageUpload

    private val _registerResult = MutableLiveData<NetworkStatus<Any>>()
    val registerResult: LiveData<NetworkStatus<Any>> get() = _registerResult


    fun imageUpload(image: MultipartBody.Part) {
        _imageUpload.value = NetworkStatus.Loading()
        disposable.add(uploadApi.uploadImage(image).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).subscribe({
                if (it.code == 200) {
                    profileUrl.value = it.data!!
                    _imageUpload.value = NetworkStatus.Success(it.data)
                } else {
                    _imageUpload.value = NetworkStatus.Error(throwable = Throwable(it.message))
                }
            }, {
                _imageUpload.value = NetworkStatus.Error(throwable = it)
                Log.e("dfadf", " ${it}")
            }))
    }

    fun register() {
        _registerResult.value = NetworkStatus.Loading()

        val request = RegisterRequest(id.value!!, password.value!!, name.value!!, profileUrl.value!!)

        disposable.add(authApi.register(request).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).subscribe({
                if (it.code == 200) {
                    _registerResult.value = NetworkStatus.Success(it.data)
                } else {
                    _registerResult.value = NetworkStatus.Error(throwable = Throwable(it.message))
                }
            }, {
                _registerResult.value = NetworkStatus.Error(throwable = it)
                Log.e("dfadf", " ${it}")
            }))
    }

    fun getProfileImageFilePart(file: File): MultipartBody.Part {
        val fileBody = RequestBody.create("image/jpeg".toMediaTypeOrNull
            (), file)

        return MultipartBody.Part.createFormData("file",
            file.name,
            fileBody)
    }
}