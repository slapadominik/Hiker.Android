package com.hiker.data.api

import com.google.gson.annotations.SerializedName
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.GET
import retrofit2.http.Query


class TouristAttraction(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("lat") val lat: Double,
    @SerializedName("long") val long: Double
)


interface TouristAttractionService {

    @GET("api/touristattraction")
    fun getByLocation(@Query("lat") latitude: Double,
                      @Query("long") longitude: Double,
                      @Query("radius") radius: Int):
            Observable<TouristAttraction>
}