package com.genzsage.core.network.dto
import kotlinx.serialization.Serializable

@Serializable
data class GlobalResponseDTO<T>(

    val success: Boolean,
    val statusCode: Int,
    val message: String,
    val timestamp: String,

    // The successful payload
    val data: T?,

    // The list of errors (only populated on failure)
    val errors: List<String>?
)