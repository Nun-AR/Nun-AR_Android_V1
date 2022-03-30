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

class SearchResultViewModel : ViewModel() {
    private val disposable = CompositeDisposable()

    private val _searchResult = MutableLiveData<NetworkStatus<List<PostResponse>>>()
    val searchResult: LiveData<NetworkStatus<List<PostResponse>>> = _searchResult

    fun getSearchList(searchWord: String) {
        _searchResult.value = NetworkStatus.Loading()

        disposable.add(
            Server.postApi.getSearchResult(searchWord).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(
                    {
                        if (it.code == 200) {

                            val result = it.data.filter {
                                it.title.contains(searchWord) || it.tag.split("#")
                                    .map { it.replace(" ", "") }.any { it.contains(searchWord) }
                            }

                            _searchResult.value = NetworkStatus.Success(result)
                        } else {
                            _searchResult.value =
                                NetworkStatus.Error(throwable = Throwable(it.message))
                        }
                    }, {
                        _searchResult.value = NetworkStatus.Error(throwable = it)
                    }
                )
        )
    }
}