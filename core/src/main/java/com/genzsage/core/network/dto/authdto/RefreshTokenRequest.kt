package com.genzsage.core.network.dto.authdto

import kotlinx.serialization.Serializable


@Serializable
data class RefreshTokenRequest(
    private val refreshToken: String,
    private val deviceId:String,
    private val otherMetaData: String
)