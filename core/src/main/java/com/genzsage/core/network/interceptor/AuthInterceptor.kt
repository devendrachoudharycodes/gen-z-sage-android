package com.genzsage.core.network.interceptor

import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject



class AuthInterceptor @Inject constructor(
    private val tokenRepository: TokenRepoInterface
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        val noAuthHeader = request.header("No-Auth")

        // Public request
        if (noAuthHeader != null) {
            val cleanRequest = request.newBuilder()
                .removeHeader("No-Auth")
                .build()

            return chain.proceed(cleanRequest)
        }

        // Private request
        val token = tokenRepository.getAccessTokenDirect()

        val authenticatedRequest = request.newBuilder().apply {
            if (!token.isNullOrBlank()) {
                addHeader("Authorization", "Bearer $token")
            }
        }.build()

        return chain.proceed(authenticatedRequest)
    }
}