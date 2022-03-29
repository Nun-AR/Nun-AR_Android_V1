package com.nunar.nun_ar_android_v1.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.nunar.nun_ar_android_v1.model.Server.postApi
import com.nunar.nun_ar_android_v1.model.Server.userApi
import com.nunar.nun_ar_android_v1.model.api.User
import com.nunar.nun_ar_android_v1.model.response.PostResponse
import com.nunar.nun_ar_android_v1.model.response.UserResponse
import com.nunar.nun_ar_android_v1.utils.NetworkStatus
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class HomeViewModel : ViewModel() {

    private val disposable = CompositeDisposable()

    private val _popularPostListResult = MutableLiveData<NetworkStatus<List<PostResponse>>>()
    val popularPostResult: LiveData<NetworkStatus<List<PostResponse>>> = _popularPostListResult

    private val _recentPostListResult = MutableLiveData<NetworkStatus<List<PostResponse>>>()
    val recentPostListResult: LiveData<NetworkStatus<List<PostResponse>>> = _recentPostListResult

    private val _userResult = MutableLiveData<NetworkStatus<UserResponse>>()
    val userResult: LiveData<NetworkStatus<UserResponse>> get() = _userResult

    init {
        refresh()
    }


    fun refresh() {
        getPopularPostList()
        getRecentPostList()
        getUserData()
    }

    private fun getUserData() {
        _userResult.value = NetworkStatus.Loading()
        disposable.add(userApi.getMyInfo().subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).subscribe(
            {
                if (it.code == 200) {
                    _userResult.value = NetworkStatus.Success(it.data)
                } else {
                    _userResult.value = NetworkStatus.Error(throwable = Throwable(it.message))
                }
            }, {
                _userResult.value = NetworkStatus.Error(throwable = it)
            }
        ))
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

    private fun getRecentPostList() {
        _recentPostListResult.value = NetworkStatus.Loading()
        disposable.add(
            postApi.getAllPost().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(
                    {
                        if (it.code == 200) {
                            _recentPostListResult.value = NetworkStatus.Success(it.data)
                        } else {
                            _recentPostListResult.value =
                                NetworkStatus.Error(throwable = Throwable(it.message))
                        }
                    }, {
                        _recentPostListResult.value = NetworkStatus.Error(throwable = it)
                    }
                )
        )
    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }
}