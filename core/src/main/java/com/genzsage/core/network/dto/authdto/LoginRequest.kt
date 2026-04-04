package com.genzsage.core.network.dto.authdto


import kotlinx.serialization.Serializable



@Serializable
data class LoginRequest (
    val identifier:String,
    val password:String,
    // Device security fields
    val deviceInfo: String,
    val otherMeta: String

)