package com.hiker.presentation.user

import android.graphics.Color
import android.widget.ImageView
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.hiker.domain.entities.User
import com.hiker.data.remote.api.ApiConsts
import com.hiker.domain.repository.UserRepository
import com.squareup.picasso.Picasso
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import java.util.*
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import com.makeramen.roundedimageview.RoundedTransformationBuilder


class UserViewModel(private val userRepository: UserRepository) : ViewModel(){


    fun getUser(userSystemId: UUID) : LiveData<User?> = runBlocking(Dispatchers.IO) {
        userRepository.getUserBySystemId(userSystemId)
    }

    fun setMountainThumbnail(imageView: ImageView, userFacebookId: String?){
        if (userFacebookId != null){
            val transformation = RoundedTransformationBuilder()
                .cornerRadiusDp(40f)
                .oval(true)
                .build()
            Picasso.get()
                .load(buildImageUri(userFacebookId)).fit()
                .transform(transformation)
                .into(imageView)
        }
    }

    private fun buildImageUri(userFacebookId: String):String{
        return ApiConsts.FacebookAPI+ "$userFacebookId/picture?width=500&height=500";
    }
}