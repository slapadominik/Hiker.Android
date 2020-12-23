package com.hiker.data.remote.api

import com.hiker.BuildConfig
import com.hiker.data.remote.dto.query.LoginQuery
import com.hiker.data.remote.dto.query.LoginQueryResponse
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST
import java.util.concurrent.TimeUnit

interface AuthenticationService {

    @POST("authentication/login")
    suspend fun login(@Body loginQuery: LoginQuery) : Response<LoginQueryResponse>

    companion object {
        fun create(): AuthenticationService {
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
                .create(AuthenticationService::class.java)
        }
    }
}