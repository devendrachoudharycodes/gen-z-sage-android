package com.genzsage.core.network.dto.authdto

import kotlinx.serialization.Serializable

@Serializable
data class LogoutRequest (
    val refreshToken: String
)
