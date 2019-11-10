package com.hiker.data.remote.api

import com.google.gson.GsonBuilder
import com.hiker.BuildConfig
import com.hiker.data.remote.dto.command.TripCommand
import com.hiker.data.remote.dto.TripBrief
import com.hiker.data.remote.dto.TripParticipant
import com.hiker.data.remote.dto.query.TripQuery
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import java.util.concurrent.TimeUnit


interface TripsService {

    @GET("trips/{tripId}")
    suspend fun getTripDetails(@Path("tripId") tripId:Int) : Response<TripQuery>

    @GET("users/{userId}/trips")
    suspend fun getUserIncomingTripsBriefs(
        @Path("userId") userId: String, @Query("dateFrom") dateFrom: String) : Response<List<TripBrief>>

    @GET("users/{userId}/trips")
    suspend fun getUserHistoryTripsBriefs(
        @Path("userId") userId: String, @Query("dateTo") dateTo: String) : Response<List<TripBrief>>

    @GET("trips")
    suspend fun getTripBriefs(@Query("tripDestinationType") tripDestinationType: Int,
                              @Query("mountainId") mountainId: Int?,
                              @Query("mountainId") rockId: Int?,
                              @Query("dateFrom") dateFrom: String) : Response<List<TripBrief>>

    @POST("trips")
    suspend fun addTrip(@Body tripCommand: TripCommand) : Response<Int>

    @POST("trips/{tripId}/tripParticipants")
    suspend fun addTripParticipant(@Path("tripId") tripId: Int,
                                   @Body tripParticipant: TripParticipant) : Response<Void>

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