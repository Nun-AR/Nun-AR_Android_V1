package com.nunar.nun_ar_android_v1.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.nunar.nun_ar_android_v1.model.Server
import com.nunar.nun_ar_android_v1.model.request.LoginRequest
import com.nunar.nun_ar_android_v1.utils.NetworkStatus
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class LoginViewModel : ViewModel() {

    private val disposable = CompositeDisposable()

    private val _loginResult = MutableLiveData<NetworkStatus<String>>()
    val loginResult: LiveData<NetworkStatus<String>> by this::_loginResult

    val id = MutableLiveData<String>()
    val password = MutableLiveData<String>()

    fun checkLogin() {
        if(!id.value.isNullOrBlank() && !password.value.isNullOrBlank()) {
            login(id.value!!, password.value!!)
        } else {
            _loginResult.value = NetworkStatus.Error(throwable = Throwable("ID 또는 PW를 입력해 주세요"))
        }
    }

    private fun login(id: String, password: String) {
        _loginResult.value = NetworkStatus.Loading()
        disposable.add(
            Server.authApi.login(LoginRequest(id, password))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    if (it.code == 200) {
                        _loginResult.postValue(NetworkStatus.Success(it.data))
                    } else {
                        val throwable = Throwable(it.message)
                        _loginResult.postValue(NetworkStatus.Error(throwable = throwable))
                    }
                }, {
                    _loginResult.postValue(NetworkStatus.Error(throwable = it))
                })
        )
    }

}