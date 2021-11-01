package com.nunar.nun_ar_android_v1.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.nunar.nun_ar_android_v1.model.Server.postApi
import com.nunar.nun_ar_android_v1.model.Server.uploadApi
import com.nunar.nun_ar_android_v1.model.request.PostRequest
import com.nunar.nun_ar_android_v1.utils.NetworkStatus
import com.nunar.nun_ar_android_v1.widget.SingleLiveEvent
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

class WritePostViewModel : ViewModel() {

    private val disposable = CompositeDisposable()

    private val _uploadFileResult = MutableLiveData<NetworkStatus<String>>()
    val uploadFileResult: LiveData<NetworkStatus<String>> = _uploadFileResult


    private val _uploadImageResult = MutableLiveData<NetworkStatus<String>>()
    val uploadImageResult: LiveData<NetworkStatus<String>> = _uploadImageResult


    val title = MutableLiveData<String>()
    val tag = MutableLiveData<String>()
    val modelFile = MutableLiveData<File>()
    val imageFile = MutableLiveData<File>()

    val imageUrl = MutableLiveData<String>()
    val modelUrl = MutableLiveData<String>()
    val postIdx = MutableLiveData<Int>()

    private val _writePostResult = MutableLiveData<NetworkStatus<Int>>()
    val writePostResult: LiveData<NetworkStatus<Int>> = _writePostResult

    fun writePost() {
        Log.e("dfdsdfda", modelUrl.value.toString())
        disposable.add(postApi.writePost(PostRequest(title.value!!,
            tag.value!!,
            imageUrl.value.toString(),
            modelUrl.value.toString(), true))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).subscribe({
                if (it.code == 200) {
                    _writePostResult.value = NetworkStatus.Success(it.data)
                    postIdx.value = it.data!!
                    Log.e("idx", postIdx.value.toString())
                } else {
                    _writePostResult.value = NetworkStatus.Error(throwable = Throwable(it.message))
                }
            }, {
                _writePostResult.value = NetworkStatus.Error(throwable = it)

                Log.e("dfadf", " ${it}")
            }))
    }

    fun uploadFile(file: MultipartBody.Part) {
        disposable.add(uploadApi.uploadModel(file).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).subscribe({
                if (it.code == 200) {
                    _uploadFileResult.value = NetworkStatus.Success(it.data)
                    modelUrl.value = it.data!!
                    writePost()
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
                if (it.code == 200) {
                    _uploadImageResult.value = NetworkStatus.Success(it.data)
                    imageUrl.value = it.data!!
                    uploadFile(getModelFilePart(modelFile.value!!))
                } else {
                    _uploadImageResult.value =
                        NetworkStatus.Error(throwable = Throwable(it.message))
                }
            }, {
                _uploadImageResult.value = NetworkStatus.Error(throwable = it)
            }))
    }

    fun getImageFilePart(file: File): MultipartBody.Part {
        val fileBody = RequestBody.create("image/jpeg".toMediaTypeOrNull
            (), file)

        return MultipartBody.Part.createFormData("file",
            file.name,
            fileBody)
    }

    fun getModelFilePart(file: File): MultipartBody.Part {
        val fileBody = RequestBody.create("model/gltf+json".toMediaTypeOrNull(), file)

        return MultipartBody.Part.createFormData("file",
            file.name,
            fileBody)
    }

    fun startWritePost() {
        uploadImage(getImageFilePart(imageFile.value!!))
    }


}