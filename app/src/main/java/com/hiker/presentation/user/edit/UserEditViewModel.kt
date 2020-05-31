package com.hiker.presentation.user.edit

import android.widget.ImageView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hiker.data.remote.api.ApiConsts
import com.hiker.data.repository.UserRepository
import com.hiker.domain.entities.Resource
import com.hiker.domain.entities.User
import com.makeramen.roundedimageview.RoundedTransformationBuilder
import com.squareup.picasso.Picasso
import kotlinx.coroutines.launch
import java.util.*

class UserEditViewModel(private val userRepository: UserRepository) : ViewModel() {

    fun getUsersData(userSystemId: UUID) : LiveData<Resource<User>>{
        val liveData = MutableLiveData<Resource<User>>()
        viewModelScope.launch {
            liveData.postValue(userRepository.getUserBySystemId(userSystemId))
        }
        return liveData
    }

    fun editUser(user: User) : LiveData<UUID>{
        val viewModel = MutableLiveData<UUID>()
        viewModelScope.launch {
            viewModel.postValue(userRepository.editUser(user))
        }
        return viewModel
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