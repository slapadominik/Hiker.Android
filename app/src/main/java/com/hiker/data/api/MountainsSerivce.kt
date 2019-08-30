package com.hiker.data.api

import com.hiker.data.dto.Mountain
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query


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
            val baseUrl = "http://192.168.0.171:5000/api/"
            return Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(MountainsSerivce::class.java)
        }
    }
}