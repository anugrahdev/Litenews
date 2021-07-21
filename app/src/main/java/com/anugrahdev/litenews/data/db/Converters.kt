package com.anugrahdev.litenews.data.db

import androidx.room.TypeConverter
import com.anugrahdev.litenews.menu.home.models.Source

class Converters {
    @TypeConverter
    fun fromSource(source: Source):String{
        return source.name!!
    }

    @TypeConverter
    fun toSource(name:String): Source {
        return Source(name, name)
    }
}