package shvyn22.flexinglyrics.api

import androidx.annotation.Keep

@Keep
data class ApiResponse<T>(
    val success: Boolean,
    val result: T
)