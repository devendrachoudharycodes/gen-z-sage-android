package com.genzsage.core.local.repository


import androidx.datastore.preferences.core.stringPreferencesKey
import com.genzsage.core.domain.repository.TokenRepository
import com.genzsage.core.local.datastore.auth.TokenManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class TokenRepositoryImpl @Inject constructor(
    private val tokenManager: TokenManager
) : TokenRepository {

    private val REFRESHTOKENKEY = stringPreferencesKey("refresh_token_secure")

    private var accessToken: String? = null

    override fun getAccessToken(): Flow<String?> = flow {
        emit(accessToken)
    }

    override fun getRefreshToken(): Flow<String?> {
        // Observe DataStore changes directly
        return tokenManager.refreshToken
    }

    override suspend fun saveTokens(accessToken: String, refreshToken: String) {
        this.accessToken = accessToken
        tokenManager.saveTokens(refreshToken)
    }

    override suspend fun clearTokens() {
        accessToken = null
        tokenManager.clearTokens()
    }

    override fun getAccessTokenDirect(): String? {
        return accessToken
    }
}