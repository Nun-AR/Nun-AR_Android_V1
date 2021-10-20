package com.nunar.nun_ar_android_v1.utils

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore

private const val TOKEN_PREFERENCES_NAME = "token_preferences"
private const val SEARCH_WORD_PREFERENCES_NAME = "search_word_preferences"

val Context.tokenDataStore: DataStore<Preferences> by preferencesDataStore(name = TOKEN_PREFERENCES_NAME)

val Context.searchWordDateStore: DataStore<Preferences> by preferencesDataStore(name = SEARCH_WORD_PREFERENCES_NAME)