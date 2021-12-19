package shvyn22.flexinglyrics

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class FlexingLyrics : Application() {
    companion object {
        const val BASE_COVER_URL = "https://api.happi.dev/v1/music/cover/"
        const val ERROR_FETCHING_DATA = "Error fetching data"
    }
}