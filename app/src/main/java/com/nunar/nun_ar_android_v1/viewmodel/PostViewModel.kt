package com.nunar.nun_ar_android_v1.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.nunar.nun_ar_android_v1.model.Server
import com.nunar.nun_ar_android_v1.model.response.PostResponse
import com.nunar.nun_ar_android_v1.utils.NetworkStatus
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class PostViewModel: ViewModel() {

    private val disposable = CompositeDisposable()

    private val _indexPostResult = MutableLiveData<NetworkStatus<List<PostResponse>>>()
    val indexPostResult: LiveData<NetworkStatus<List<PostResponse>>> = _indexPostResult

    init {
        getIdxPostResult(0)
    }

    fun getIdxPostResult(idx: Int){
        _indexPostResult.value = NetworkStatus.Loading()

        disposable.add(
            Server.postApi.getPostByIdx(idx).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(
                    {
                        if (it.code == 200) {
                            _indexPostResult.value = NetworkStatus.Success(it.data)
                        } else {
                            _indexPostResult.value =
                                NetworkStatus.Error(throwable = Throwable(it.message))
                        }
                    }, {
                        _indexPostResult.value = NetworkStatus.Error(throwable = it)
                    }
                )
        )
    }
}