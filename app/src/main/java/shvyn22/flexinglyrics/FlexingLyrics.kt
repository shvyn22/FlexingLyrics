package shvyn22.flexinglyrics

import android.app.Application
import shvyn22.flexinglyrics.di.component.DaggerSingletonComponent
import shvyn22.flexinglyrics.di.component.SingletonComponent

class FlexingLyrics : Application() {

    lateinit var singletonComponent: SingletonComponent
        private set

    override fun onCreate() {
        super.onCreate()
        singletonComponent = DaggerSingletonComponent.factory()
            .create(this)
    }

    companion object {
        const val BASE_COVER_URL = "https://api.happi.dev/v1/music/cover/"
        const val ERROR_FETCHING_DATA = "Error fetching data"
    }
}