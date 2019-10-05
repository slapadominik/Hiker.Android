package com.hiker.presentation.user

import android.util.Log
import android.widget.ImageView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hiker.data.models.User
import com.hiker.data.remote.api.ApiConsts
import com.hiker.domain.repository.UserRepository
import com.squareup.picasso.Picasso
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import java.util.*

class UserViewModel(private val userRepository: UserRepository) : ViewModel(){

    fun getUser(userSystemId: UUID) : LiveData<User?> = runBlocking(Dispatchers.IO) {
        userRepository.getUserBySystemId(userSystemId)
    }

    fun setMountainThumbnail(imageView: ImageView, userFacebookId: String?){
        if (userFacebookId != null){
            Picasso.get()
                .load(buildImageUri(userFacebookId))
                .into(imageView)
        }
    }

    private fun buildImageUri(userFacebookId: String):String{
        return ApiConsts.FacebookAPI+ "$userFacebookId/picture?width=500&height=500";
    }
}