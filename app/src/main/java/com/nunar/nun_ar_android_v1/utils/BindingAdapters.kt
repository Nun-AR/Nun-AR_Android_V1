package com.nunar.nun_ar_android_v1.utils

import android.view.View
import androidx.databinding.BindingAdapter

object BindingAdapters {

    @JvmStatic
    @BindingAdapter("isSelected")
    fun setSelected(view: View, selected: Boolean) {
        view.isSelected = selected
    }

    @JvmStatic
    @BindingAdapter("isVisible")
    fun setVisibility(view: View, visible: Boolean) {
        view.visibility = if(visible) View.VISIBLE else View.GONE
    }
}