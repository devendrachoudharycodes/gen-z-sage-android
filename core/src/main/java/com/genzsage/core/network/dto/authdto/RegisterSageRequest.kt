package com.genzsage.core.network.dto.authdto


import kotlinx.serialization.Serializable

@Serializable
data class RegisterSageRequest(
    val identity: String,
    val email: String,
    val password: String,
    val phoneNumber: String,
    val profileName: String,
    val birthDate: String, // Send as "YYYY-MM-DD" to avoid serialization errors
    val country: String,
    val bio: String,
    val isPrivate: Boolean,
    val profilePicUrl: String,

    // Device security fields
    val deviceInfo: String,
    val otherMeta: String
)