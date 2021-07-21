package com.anugrahdev.litenews.utils

import android.annotation.SuppressLint
import android.icu.util.ULocale.getCountry
import android.os.Build
import androidx.annotation.RequiresApi
import org.ocpsoft.prettytime.PrettyTime
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


object Utils {

    @RequiresApi(Build.VERSION_CODES.N)
    fun DateToTimeFormat(oldstringDate: String): String {
        val p = PrettyTime(Locale.getDefault())
        var isTime: String? = null
        try {
            val sdf = SimpleDateFormat(
                "yyyy-MM-dd'T'HH:mm:ss'Z'",
                Locale.getDefault()
            )
            val date: Date? = sdf.parse(oldstringDate)
            isTime = p.format(date)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return isTime ?: ""
    }

    @SuppressLint("SimpleDateFormat")
    @RequiresApi(Build.VERSION_CODES.N)
    fun DateFormat(oldstringDate: String): String? {
        val newDate: String?
        val dateFormat =
            SimpleDateFormat("E, d MMM yyyy", Locale.getDefault())
        newDate = try {
            val date: Date? = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").parse(oldstringDate)
            dateFormat.format(date!!)
        } catch (e: ParseException) {
            e.printStackTrace()
            oldstringDate
        }
        return newDate
    }

    fun fixingTitle(title: String): String{
        return title.substring(0, title.indexOf(" - "));
    }
}