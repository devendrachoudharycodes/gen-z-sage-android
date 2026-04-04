package com.genzsage.core.network.interceptor

import com.genzsage.core.local.di.DeviceId
import com.genzsage.core.local.di.DeviceInfo
import com.genzsage.core.network.dto.authdto.RefreshTokenRequest
import com.genzsage.core.domain.repository.TokenRepository
import com.genzsage.core.network.api.AuthApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import javax.inject.Inject
import javax.inject.Provider
import javax.inject.Singleton

@Singleton
class TokenAuthenticator @Inject constructor(
    private val tokenRepository: TokenRepository,
    private val authApiProvider: Provider<AuthApi>,
    @DeviceId private val deviceId: String,
    @DeviceInfo private val deviceInfo: String
) : Authenticator {

    override fun authenticate(
        route: Route?,
        response: Response
    ): Request? {

        // Prevent infinite refresh loops
        if (responseCount(response) >= 2) {
            runBlocking {
                tokenRepository.clearTokens()
            }
            return null
        }

        synchronized(this) {
            return try {
                runBlocking {

                    val failedToken = response.request
                        .header("Authorization")
                        ?.removePrefix("Bearer ")
                        ?.trim()

                    val currentToken = tokenRepository
                        .getAccessToken()
                        .first()

                    // Another request already refreshed token
                    if (
                        !currentToken.isNullOrBlank() &&
                        currentToken != failedToken
                    ) {
                        return@runBlocking response.request
                            .newBuilder()
                            .removeHeader("Authorization")
                            .addHeader(
                                "Authorization",
                                "Bearer $currentToken"
                            )
                            .build()
                    }

                    val refreshToken = tokenRepository
                        .getRefreshToken()
                        .first()

                    if (refreshToken.isNullOrBlank()) {
                        tokenRepository.clearTokens()
                        return@runBlocking null
                    }

                    val authApi = authApiProvider.get()

                    val refreshResponse =
                        authApi.refreshToken(
                            RefreshTokenRequest(
                                refreshToken = refreshToken,
                                deviceId.toString(),
                                deviceInfo.toString()

                            )
                        )

                    val newAccessToken =
                        refreshResponse.body()?.data?.accessToken?:""


                    val newRefreshToken =
                        refreshResponse.body()?.data?.refreshToken?:""

                    if (
                        newAccessToken.isBlank() ||
                        newRefreshToken.isBlank()
                    ) {
                        tokenRepository.clearTokens()
                        return@runBlocking null
                    }

                    tokenRepository.saveTokens(
                        newAccessToken,
                        newRefreshToken
                    )

                    return@runBlocking response.request
                        .newBuilder()
                        .removeHeader("Authorization")
                        .addHeader(
                            "Authorization",
                            "Bearer $newAccessToken"
                        )
                        .build()
                }
            } catch (e: Exception) {
                runBlocking {
                    tokenRepository.clearTokens()
                }
                null
            }
        }
    }

    private fun responseCount(
        response: Response
    ): Int {
        var count = 1
        var current = response.priorResponse

        while (current != null) {
            count++
            current = current.priorResponse
        }

        return count
    }
}