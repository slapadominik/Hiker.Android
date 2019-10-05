package com.hiker.data.remote.api

import com.hiker.BuildConfig
import com.hiker.data.remote.dto.FacebookToken
import com.hiker.data.remote.dto.User
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import java.util.*
import java.util.concurrent.TimeUnit

interface UserService {

    @GET("users")
    suspend fun getUserByFacebookId(
        @Query("facebookId") facebookId: String
    ) : Response<User>

    @GET("users")
    suspend fun getUserBySystemId(
        @Query("userSystemId") userSystemId: UUID
    ) : Response<User>

    @POST("authentication/login/facebook")
    suspend fun addUser(@Body facebookToken: FacebookToken) : Response<UUID>

    companion object {
        fun create(): UserService {
            val okHttpClientBuilder = OkHttpClient.Builder()
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)

            if (BuildConfig.DEBUG) {
                val logging = HttpLoggingInterceptor()
                logging.level = HttpLoggingInterceptor.Level.BASIC
                okHttpClientBuilder.addInterceptor(logging)
            }

            return Retrofit.Builder()
                .baseUrl(ApiConsts.HikerAPI)
                .client(okHttpClientBuilder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(UserService::class.java)
        }
    }
}