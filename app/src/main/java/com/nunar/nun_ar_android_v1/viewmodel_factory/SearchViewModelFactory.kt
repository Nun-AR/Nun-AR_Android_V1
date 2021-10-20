package com.nunar.nun_ar_android_v1.viewmodel_factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.nunar.nun_ar_android_v1.repository.SearchRepository
import com.nunar.nun_ar_android_v1.viewmodel.SearchViewModel

class SearchViewModelFactory(private val searchRepository: SearchRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T = SearchViewModel(searchRepository) as T
}