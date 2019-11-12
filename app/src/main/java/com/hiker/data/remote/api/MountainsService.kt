package com.hiker.data.remote.api

import com.hiker.data.remote.dto.Mountain
import com.hiker.data.remote.dto.MountainBrief
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import java.util.concurrent.TimeUnit


interface MountainsService {

    @GET("mountains/{id}")
    suspend fun getById(@Path("id") id: Int): Response<Mountain>

    @GET("mountains")
    suspend fun getAll(): Response<List<MountainBrief>>

    companion object {
        fun create(): MountainsService {
            val client = OkHttpClient.Builder()
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .build()
            return Retrofit.Builder()
                .baseUrl(ApiConsts.HikerAPI)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
                .create(MountainsService::class.java)
        }
    }
}