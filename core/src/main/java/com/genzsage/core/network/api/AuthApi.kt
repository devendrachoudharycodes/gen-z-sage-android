package com.genzsage.core.network.api

import com.genzsage.core.network.dto.GlobalResponseDTO
import com.genzsage.core.network.dto.authdto.AuthResponse
import com.genzsage.core.network.dto.authdto.LoginRequest
import com.genzsage.core.network.dto.authdto.LogoutRequest
import com.genzsage.core.network.dto.authdto.RefreshTokenRequest
import com.genzsage.core.network.dto.authdto.RegisterSageRequest
import com.genzsage.core.network.dto.authdto.SageAvailabilityRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST






interface AuthApi {
    @Headers("No-Auth: true")
    @POST("/api/v1/auth/checkAvailability")
    suspend fun checkAvailability(
        @Body request: SageAvailabilityRequest
    ): Response<GlobalResponseDTO<Boolean>>

    @Headers("No-Auth: true")
    @POST("/api/v1/auth/register")
    suspend fun register(
        @Body request: RegisterSageRequest
    ): Response<GlobalResponseDTO<AuthResponse>>

    @Headers("No-Auth: true")
    @POST("/api/v1/auth/login")
    suspend fun login(
        @Body request: LoginRequest
    ): Response<GlobalResponseDTO<AuthResponse>>

    @Headers("No-Auth: true")
    @POST("/api/v1/auth/refresh")
    suspend fun refreshToken(
        @Body request: RefreshTokenRequest
    ): Response<GlobalResponseDTO<AuthResponse>>

    @Headers("No-Auth: true")
    @POST("/api/v1/auth/logout")
    suspend fun logout(
        @Body request: LogoutRequest
    ): Response<GlobalResponseDTO<Boolean>>


}