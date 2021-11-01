package com.nunar.nun_ar_android_v1.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.nunar.nun_ar_android_v1.model.Server
import com.nunar.nun_ar_android_v1.model.Server.bookmarkApi
import com.nunar.nun_ar_android_v1.model.Server.postApi
import com.nunar.nun_ar_android_v1.model.response.PostResponse
import com.nunar.nun_ar_android_v1.utils.NetworkStatus
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class PostViewModel : ViewModel() {

    private val disposable = CompositeDisposable()

    private val _indexPostResult = MutableLiveData<NetworkStatus<PostResponse>>()
    val indexPostResult: LiveData<NetworkStatus<PostResponse>> = _indexPostResult

    private val _popularPostListResult = MutableLiveData<NetworkStatus<List<PostResponse>>>()
    val popularPostResult: LiveData<NetworkStatus<List<PostResponse>>> = _popularPostListResult

    private val _bookmarkResult = MutableLiveData<NetworkStatus<Boolean>>()
    val bookmarkResult: LiveData<NetworkStatus<Boolean>> = _bookmarkResult

    val isBookmark = MutableLiveData<Boolean>()


    init {
        getPopularPostList()
    }

    fun getIdxPostResult(idx: Int) {
        _indexPostResult.value = NetworkStatus.Loading()

        disposable.add(
            postApi.getPostByIdx(idx).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(
                    {
                        if (it.code == 200) {
                            _indexPostResult.value = NetworkStatus.Success(it.data)
                            isBookmark.value = it.data.isBookmarks
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

    private fun getPopularPostList() {
        _popularPostListResult.value = NetworkStatus.Loading()
        disposable.add(
            postApi.getPopularPost().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(
                    {
                        if (it.code == 200) {
                            _popularPostListResult.value = NetworkStatus.Success(it.data)
                        } else {
                            _popularPostListResult.value =
                                NetworkStatus.Error(throwable = Throwable(it.message))
                        }
                    }, {
                        _popularPostListResult.value = NetworkStatus.Error(throwable = it)
                    }
                )
        )
    }

    fun postBookmark() {
        (indexPostResult.value as? NetworkStatus.Success)?.data?.apply {
            disposable.add(
                bookmarkApi.postBookmark(postIdx)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe({
                        if(it.code == 200) {
                            isBookmark.value = true
                            _bookmarkResult.value = NetworkStatus.Success(true)
                        } else {
                            _bookmarkResult.value = NetworkStatus.Error(throwable = Throwable(it.message))
                        }
                    }, {
                        _bookmarkResult.value = NetworkStatus.Error(throwable = it)
                    })
            )
        }
    }

    fun deleteBookmark() {
        (indexPostResult.value as? NetworkStatus.Success)?.data?.apply {
            disposable.add(
                bookmarkApi.deleteBookmark(postIdx)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe({
                        if(it.code == 200) {
                            isBookmark.value = false
                            _bookmarkResult.value = NetworkStatus.Success(false)
                        } else {
                            _bookmarkResult.value = NetworkStatus.Error(throwable = Throwable(it.message))
                        }
                    }, {
                        _bookmarkResult.value = NetworkStatus.Error(throwable = it)
                    })
            )

        }
    }
}