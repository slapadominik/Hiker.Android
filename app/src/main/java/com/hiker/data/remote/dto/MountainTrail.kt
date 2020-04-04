package com.hiker.data.remote.dto

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
class MountainTrail(
    @SerializedName("mountainId") val id: Int,
    @SerializedName("timeToTopMinutes") val timeToTopMinutes: Float,
    @SerializedName("color") val color: String
) : Parcelable