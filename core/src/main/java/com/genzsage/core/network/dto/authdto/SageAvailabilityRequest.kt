package com.genzsage.core.network.dto.authdto

import kotlinx.serialization.Serializable


@Serializable
data class SageAvailabilityRequest (
    val identity: String,
    val email: String,
    val phoneNumber: String,
    val country: String
)