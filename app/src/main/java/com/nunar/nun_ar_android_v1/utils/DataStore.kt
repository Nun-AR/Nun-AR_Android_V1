package com.nunar.nun_ar_android_v1.utils

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore

private const val TOKEN_PREFERENCES_NAME = "token_preferences"

val Context.tokenDataStore: DataStore<Preferences> by preferencesDataStore(name = TOKEN_PREFERENCES_NAME)