package shvyn22.lyricsapplication

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class LyricsApplication : Application() {
    companion object {
        const val BASE_COVER_URL = "https://api.happi.dev/v1/music/cover/"
        const val ERROR_FETCHING_DATA = "Error fetching data"
    }
}