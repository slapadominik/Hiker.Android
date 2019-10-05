package com.hiker.data.remote.api

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Streaming
import retrofit2.http.Url


interface FacebookService {

    @Streaming
    @GET()
    suspend fun downloadFileByUrlRx(@Url fileUrl: String): Response<ResponseBody>

    companion object {
        fun create(): MountainsSerivce {
            return Retrofit.Builder()
                .baseUrl(ApiConsts.FacebookAPI)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(MountainsSerivce::class.java)
        }
    }
}