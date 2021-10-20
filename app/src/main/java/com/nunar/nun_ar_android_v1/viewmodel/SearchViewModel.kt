package com.nunar.nun_ar_android_v1.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.nunar.nun_ar_android_v1.model.Server.postApi
import com.nunar.nun_ar_android_v1.repository.SearchRepository
import com.nunar.nun_ar_android_v1.utils.NetworkStatus
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect

class SearchViewModel(private val searchRepository: SearchRepository) : ViewModel() {

    private val disposable = CompositeDisposable()

    private val _suggestionTagResult = MutableLiveData<NetworkStatus<List<String>>>()
    val suggestionTagResult: LiveData<NetworkStatus<List<String>>> = _suggestionTagResult

    private val _recentSearchWordResult = MutableLiveData<NetworkStatus<List<String>>>()
    val recentSearchWordResult: LiveData<NetworkStatus<List<String>>> = _recentSearchWordResult

    init {
        getSuggestionTagList()
    }

    fun getSuggestionTagList() {
        disposable.add(
            postApi.getPopularPost()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response ->
                    if (response.code == 200) {
                        _suggestionTagResult.value = NetworkStatus.Success(
                            response.data.map {
                                it.tag.split("#").filter { tag -> tag.isNotBlank() }
                                    .map { tag -> "#${tag.trim()}" }
                            }.fold(listOf<String>(), { total, list -> total.plus(list) }).distinct()
                        )
                    } else {
                        _suggestionTagResult.value =
                            NetworkStatus.Error(throwable = Throwable(response.message))
                    }
                }, {
                    _suggestionTagResult.value = NetworkStatus.Error(throwable = it)
                })
        )
    }

    suspend fun getRecentSearchWord() {
        searchRepository.getRecentSearchWord()
            .catch { cause: Throwable ->
                _recentSearchWordResult.value = NetworkStatus.Error(throwable = cause)
            }
            .collect {
                _recentSearchWordResult.value = NetworkStatus.Success(it)
            }
    }

    suspend fun addRecentSearchWord(searchWord: String) {
        searchRepository.addRecentSearchWord(searchWord)
    }

    suspend fun removeAllSearchWord() {
        searchRepository.removeAllSearchWord()
    }


}