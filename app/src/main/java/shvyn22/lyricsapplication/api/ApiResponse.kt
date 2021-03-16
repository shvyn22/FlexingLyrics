package shvyn22.lyricsapplication.api

data class ApiResponse<T>(
    val success: Boolean,
    val result: T
)