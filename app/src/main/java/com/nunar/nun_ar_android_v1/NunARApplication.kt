package com.nunar.nun_ar_android_v1

import android.app.Application
import android.content.Context

class NunARApplication: Application() {

    companion object {
        var context: Context? = null
    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
    }
}