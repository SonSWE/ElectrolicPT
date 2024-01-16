package com.example.electrolicpt.Utils

import android.view.View
import androidx.annotation.NonNull
import androidx.viewpager2.widget.ViewPager2

class SliderTransform: ViewPager2.PageTransformer{
    override fun transformPage(@NonNull page: View, position: Float) {
        var r = 1 - Math.abs(position)
        page.scaleY = 0.85f + r * 0.15f
    }
}