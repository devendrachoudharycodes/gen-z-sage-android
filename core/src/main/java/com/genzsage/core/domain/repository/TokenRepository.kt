package com.genzsage.core.domain.repository

import kotlinx.coroutines.flow.Flow

interface TokenRepository {
    fun getAccessToken() : Flow<String?>
    fun getRefreshToken(): Flow<String?>
    suspend fun saveTokens(accessToken: String, refreshToken: String)
    suspend fun clearTokens()

    fun getAccessTokenDirect():String?
}