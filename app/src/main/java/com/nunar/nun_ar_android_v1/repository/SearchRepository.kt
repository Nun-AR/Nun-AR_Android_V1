package com.nunar.nun_ar_android_v1.repository

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import com.nunar.nun_ar_android_v1.utils.searchWordDateStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SearchRepository(private val context: Context) {
    private val listKey = stringSetPreferencesKey("search_word")

    fun getRecentSearchWord(): Flow<List<String>> {
        return context.searchWordDateStore.data.map {
            it[listKey]?.toList()?.reversed() ?: emptyList()
        }
    }

    suspend fun addRecentSearchWord(searchWord: String) {
        context.searchWordDateStore.edit {
            it[listKey] = it[listKey]?.plus(searchWord) ?: setOf()
        }
    }

    suspend fun removeAllSearchWord() {
        context.searchWordDateStore.edit {
            it[listKey] = setOf()
        }
    }

}