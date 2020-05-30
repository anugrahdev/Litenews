package com.anugrahdev.litenews.preferences

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager

private const val KEY_COUNTRY = "key_country"
private const val KEY_DARK_MODE = "key_dark_mode"

class PreferenceProvider(context: Context) {

    private val appContext = context.applicationContext

    private val preference: SharedPreferences
        get() = PreferenceManager.getDefaultSharedPreferences(appContext)

    fun setCountry(country:String){
        preference.edit().putString(
            KEY_COUNTRY,
            country
        ).apply()
    }

    fun getCountry():String{
        return preference.getString(KEY_COUNTRY, "ID")!!
    }

    fun setDarkMode(dark: Boolean){
        preference.edit().putBoolean(
            KEY_DARK_MODE,
            dark
        ).apply()
    }

    fun getDarkMode():Boolean{
        return preference.getBoolean(KEY_DARK_MODE, false)
    }



}