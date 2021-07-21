package com.anugrahdev.litenews.utils

import com.squareup.moshi.Moshi
import okhttp3.ResponseBody

fun <T> jsonToModel(model: Class<T>, error: ResponseBody?): T? {
    try {
        Moshi.Builder().build().also { moshi ->
            moshi.adapter(model).also { adapter ->
                return adapter.fromJson(error?.string()!!)
            }
        }
    } catch (t: Throwable) {
        throw t
    }
}