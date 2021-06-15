package shvyn22.lyricsapplication.util

import shvyn22.lyricsapplication.data.remote.Track

sealed class StateEvent {
    data class NavigateToDetails(val track: Track) : StateEvent()
    object Error : StateEvent()
    object Loading : StateEvent()
    data class NavigateToMedia(val url: String) : StateEvent()
}
