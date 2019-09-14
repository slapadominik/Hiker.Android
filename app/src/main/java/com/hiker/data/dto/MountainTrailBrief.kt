package com.hiker.data.dto

import com.google.gson.annotations.SerializedName

class MountainTrailBrief(
    @SerializedName("href") val href:String,
    @SerializedName("count") val count:Int
)