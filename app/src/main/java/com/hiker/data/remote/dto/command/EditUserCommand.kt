package com.hiker.data.remote.dto.command

import com.google.gson.annotations.SerializedName
import java.util.*

data class EditUserCommand(
    @SerializedName("firstName") val firstName: String,
    @SerializedName("lastName") val lastName: String,
    @SerializedName("birthday") val birthday: Date?,
    @SerializedName("aboutMe") val aboutMe: String?,
    @SerializedName("phoneNumber")  val phoneNumber: String?
)