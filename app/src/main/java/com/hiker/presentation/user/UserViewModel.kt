package com.hiker.presentation.user

import android.widget.ImageView
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.hiker.domain.entities.User
import com.hiker.data.remote.api.ApiConsts
import com.squareup.picasso.Picasso
import java.util.*
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.hiker.data.converters.asDbModel
import com.hiker.data.repository.UserRepository
import com.makeramen.roundedimageview.RoundedTransformationBuilder
import kotlinx.coroutines.launch


class UserViewModel(private val userRepository: UserRepository) : ViewModel(){

    fun getUser(userSystemId: UUID) : LiveData<User> {
        val liveData = MutableLiveData<User>()
        viewModelScope.launch {
            liveData.postValue(userRepository.getUserBySystemId(userSystemId))
        }
        return liveData
    }

    fun cacheUser(user: User){
        viewModelScope.launch { userRepository.cacheUser(user.asDbModel())}
    }

    fun setUserThumbnail(imageView: ImageView, userFacebookId: String?){
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