package com.hiker.data.repository

import com.hiker.data.remote.api.AuthenticationService
import com.hiker.data.remote.dto.query.LoginQuery
import com.hiker.data.remote.dto.query.LoginQueryResponse
import com.hiker.domain.consts.HttpCode
import com.hiker.domain.entities.Resource

interface IAuthenticationRepository {
    suspend fun login(loginQuery: LoginQuery) : Resource<LoginQueryResponse>
}

class AuthenticationRepository: IAuthenticationRepository{
    private val authService = AuthenticationService.create()

    override suspend fun login(loginQuery: LoginQuery): Resource<LoginQueryResponse> {
        try{
            val response = authService.login(loginQuery)
            if (response.isSuccessful){
                return Resource.success(response.body()!!)
            }
            else if (response.code() == HttpCode.Unauthorized){
                return Resource.unauthorized()
            }
            else{
                return Resource.error("API timeout", null)
            }
        }
        catch (e: Exception){
            return Resource.error("Brak połączenia z internetem", null)
        }
    }

    companion object {
        private var instance: AuthenticationRepository? = null

        @Synchronized
        fun getInstance(): AuthenticationRepository {
            if (instance == null)
                instance =
                    AuthenticationRepository()
            return instance as AuthenticationRepository
        }
    }
}