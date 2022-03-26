package shvyn22.flexinglyrics.util

import shvyn22.flexinglyrics.data.remote.Track

sealed class StateEvent {
    data class NavigateToDetails(val track: Track) : StateEvent()
    class Error(val error: StateError) : StateEvent()
    object Loading : StateEvent()
    data class NavigateToMedia(val url: String) : StateEvent()
}

enum class StateError {
    ERROR_FETCHING_DATA,
    ERROR_LOADING_IMAGE,
    ERROR_PERMISSION_NOT_GRANTED
}
