package com.nunar.nun_ar_android_v1.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.nunar.nun_ar_android_v1.model.Server.bookmarkApi
import com.nunar.nun_ar_android_v1.model.Server.postApi
import com.nunar.nun_ar_android_v1.model.Server.userApi
import com.nunar.nun_ar_android_v1.model.response.PostResponse
import com.nunar.nun_ar_android_v1.model.response.UserResponse
import com.nunar.nun_ar_android_v1.utils.NetworkStatus
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class UserInfoViewModel : ViewModel() {

    private val disposable = CompositeDisposable()

    private val _isOpenBookmarkPosts = MutableLiveData(true)
    val isOpenBookmarkPosts: LiveData<Boolean> = _isOpenBookmarkPosts

    private val _isOpenMyPosts = MutableLiveData(true)
    val isOpenMyPosts: LiveData<Boolean> = _isOpenMyPosts

    private val _getBookmarkPostResult = MutableLiveData<NetworkStatus<List<PostResponse>>>()
    val getBookmarkPostResult: LiveData<NetworkStatus<List<PostResponse>>> = _getBookmarkPostResult

    private val _getMyPostResult = MutableLiveData<NetworkStatus<List<PostResponse>>>()
    val getMyPostResult: LiveData<NetworkStatus<List<PostResponse>>> = _getMyPostResult

    private val _getMyInfoResult = MutableLiveData<NetworkStatus<UserResponse>>()
    val getMyInfoResult: LiveData<NetworkStatus<UserResponse>> = _getMyInfoResult

    init {
        getMyInfo()
        getBookmarkPost()
    }

    fun clickOpenBookmarkPosts() {
        _isOpenBookmarkPosts.value = _isOpenBookmarkPosts.value?.not()
    }

    fun clickOpenMyPosts() {
        _isOpenMyPosts.value = _isOpenMyPosts.value?.not()
    }

    fun getMyInfo() {
        disposable.add(
            userApi.getMyInfo()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    _getMyInfoResult.value =
                        if (it.code == 200) {
                            getMyPost(it.data.userIdx)
                            NetworkStatus.Success(it.data)
                        } else {
                            NetworkStatus.Error(throwable = Throwable(it.message))
                        }
                }, {
                    _getMyInfoResult.value = NetworkStatus.Error(throwable = it)
                })
        )
    }

    fun getBookmarkPost() {
        disposable.add(
            bookmarkApi.getMyBookmark()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    _getBookmarkPostResult.value =
                        if (it.code == 200) {
                            NetworkStatus.Success(it.data)
                        } else {
                            NetworkStatus.Error(throwable = Throwable(it.message))
                        }
                }, {
                    _getBookmarkPostResult.value = NetworkStatus.Error(throwable = it)
                })
        )
    }

    fun getMyPost(userIdx: Int) {
        disposable.add(
            postApi.getPostByUserIdx(userIdx)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(
                    {
                        _getMyPostResult.value =
                            if (it.code == 200) {
                                NetworkStatus.Success(it.data)
                            } else {
                                NetworkStatus.Error(throwable = Throwable(it.message))
                            }
                    }, {
                        _getMyPostResult.value = NetworkStatus.Error(throwable = it)
                    }
                )
        )
    }

}