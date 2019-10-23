package com.hiker.data.remote.api

import android.database.Cursor
import com.hiker.data.remote.dto.Mountain
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.concurrent.TimeUnit


interface MountainsSerivce {

    @GET("mountains/location")
    suspend fun getByLocation(
        @Query("lat") latitude: Double,
        @Query("long") longitude: Double,
        @Query("radius") radius: Double
    ): Response<Mountain>

    @GET("mountains")
    suspend fun getAll(): Response<List<Mountain>>

    companion object {
        fun create(): MountainsSerivce {
            val client = OkHttpClient.Builder()
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .build()
            return Retrofit.Builder()
                .baseUrl(ApiConsts.HikerAPI)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
                .create(MountainsSerivce::class.java)
        }
    }
}