package com.anugrahdev.litenews.data.db.entities


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Source(
    val id: String?,
    val name: String?
):Parcelable