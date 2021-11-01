package com.nunar.nun_ar_android_v1.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.nunar.nun_ar_android_v1.model.Server.uploadApi
import com.nunar.nun_ar_android_v1.model.Server.userApi
import com.nunar.nun_ar_android_v1.model.request.ModifyUserInfoRequest
import com.nunar.nun_ar_android_v1.utils.NetworkStatus
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

class ModifyUserInfoViewModel : ViewModel() {
    private val disposable = CompositeDisposable()

    private val _modifyUserInfoResult = MutableLiveData<NetworkStatus<Unit>>()
    val modifyUserInfoResult: LiveData<NetworkStatus<Unit>> = _modifyUserInfoResult

    val profileUrl = MutableLiveData<String>()
    val name = MutableLiveData<String>()

    val file = MutableLiveData<File>()

    fun saveInfo() {
        file.value?.let {
            if (name.value == null) {
                throw Throwable("이름을 적어주세요")
            }

            val fileBody = RequestBody.create("image/jpeg".toMediaTypeOrNull(), it)

            val uploadRequest = MultipartBody.Part.createFormData("file", it.name, fileBody)

            disposable.add(
                uploadApi.uploadImage(uploadRequest)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe({ res ->
                        if(res.code == 200) {
                            modifyUserInfo(res.data)
                        } else {
                            throw Throwable(res.message)
                        }
                    },{ t ->
                        throw t
                    })
            )
        } ?: run {
            modifyUserInfo()
        }
    }

    fun modifyUserInfo(newUrl: String? = null) {
        profileUrl.value?.let { profileUrl ->
            name.value?.let { name ->
                val body = ModifyUserInfoRequest(name, newUrl ?: profileUrl)
                disposable.add(
                    userApi.modifyUserInfo(body)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe({
                            _modifyUserInfoResult.value =
                                if (it.code == 200) {
                                    NetworkStatus.Success(it.data)
                                } else {
                                    NetworkStatus.Error(throwable = Throwable(it.message))
                                }
                        }, {
                            _modifyUserInfoResult.value = NetworkStatus.Error(throwable = it)
                        })
                )
            }
        }
    }

}