package com.example.electrolicpt.Utils

import android.text.InputFilter
import android.text.Spanned
class InputFilterMinMax : InputFilter {
    private var min: Int
    private var max: Int

    constructor(min: Int, max: Int) {
        this.min = min
        this.max = max
    }

    constructor(min: String, max: String) {
        this.min = min.toInt()
        this.max = max.toInt()
    }

    override fun filter(source: CharSequence, start: Int, end: Int, dest: Spanned, dStart: Int, dEnd: Int): CharSequence? {
        try {
            val input = Integer.parseInt(dest.toString() + source.toString())
            if (isInRange(min, max, input)) {
                return null
            }
        } catch (e: NumberFormatException) {
            e.printStackTrace()
        }
        return ""
    }

    private fun isInRange(a: Int, b: Int, c: Int): Boolean {
        return if (b > a) c in a..b else c in b..a
    }
}