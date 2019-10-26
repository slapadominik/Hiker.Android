package com.hiker.data.remote.api

import com.google.gson.GsonBuilder
import com.hiker.BuildConfig
import com.hiker.data.remote.dto.FacebookToken
import com.hiker.data.remote.dto.Trip
import com.hiker.data.remote.dto.TripBrief
import com.hiker.data.remote.dto.User
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import java.util.*
import java.util.concurrent.TimeUnit


interface TripsService {

    @GET("users/{userId}/trips")
    suspend fun getUserIncomingTripsBriefs(
        @Path("userId") userId: String, @Query("dateFrom") dateFrom: String) : Response<List<TripBrief>>

    @GET("users/{userId}/trips")
    suspend fun getUserHistoryTripsBriefs(
        @Path("userId") userId: String, @Query("dateTo") dateTo: String) : Response<List<TripBrief>>

    @POST("trips")
    suspend fun addTrip(@Body trip: Trip) : Response<Int>

    companion object {
        fun create(): TripsService {
            val okHttpClientBuilder = OkHttpClient.Builder()
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)

            if (BuildConfig.DEBUG) {
                val logging = HttpLoggingInterceptor()
                logging.level = HttpLoggingInterceptor.Level.BASIC
                okHttpClientBuilder.addInterceptor(logging)
            }
            val gson = GsonBuilder()
                .setDateFormat("yyyy-MM-dd")
                .create()

            return Retrofit.Builder()
                .baseUrl(ApiConsts.HikerAPI)
                .client(okHttpClientBuilder.build())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
                .create(TripsService::class.java)
        }
    }
}