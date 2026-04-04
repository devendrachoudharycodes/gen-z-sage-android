package com.genzsage.core.network.dto.authdto

import kotlinx.serialization.Serializable


@Serializable
data class AuthResponse (
    val accessToken: String,
    val refreshToken: String
)
