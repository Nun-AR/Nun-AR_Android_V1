package com.nunar.nun_ar_android_v1.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.nunar.nun_ar_android_v1.model.Server.uploadApi
import com.nunar.nun_ar_android_v1.utils.NetworkStatus
import com.nunar.nun_ar_android_v1.widget.SingleLiveEvent
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import okhttp3.MultipartBody

class WritePostViewModel : ViewModel() {

    private val disposable = CompositeDisposable()

    private val _uploadFileResult = MutableLiveData<NetworkStatus<String>>()
    val uploadFileResult: LiveData<NetworkStatus<String>> = _uploadFileResult


    private val _uploadImageResult = MutableLiveData<NetworkStatus<String>>()
    val uploadImageResult: LiveData<NetworkStatus<String>> = _uploadImageResult

    fun uploadFile(file: MultipartBody.Part) {
        disposable.add(uploadApi.uploadModel(file).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).subscribe({
                if (it.code == 200) {
                    _uploadFileResult.value = NetworkStatus.Success(it.data)
                } else {
                    _uploadFileResult.value = NetworkStatus.Error(throwable = Throwable(it.message))
                }
            }, {
                _uploadFileResult.value = NetworkStatus.Error(throwable = it)
            }))
    }

    fun uploadImage(image: MultipartBody.Part) {
        disposable.add(uploadApi.uploadImage(image).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).subscribe({
                if(it.code == 200){
                    _uploadImageResult.value = NetworkStatus.Success(it.data)
                } else {
                    _uploadImageResult.value = NetworkStatus.Error(throwable = Throwable(it.message))

                }
            }, {
                _uploadImageResult.value = NetworkStatus.Error(throwable = it)
            }))
    }


}