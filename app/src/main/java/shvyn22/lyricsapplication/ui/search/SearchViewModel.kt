package shvyn22.lyricsapplication.ui.search

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import shvyn22.lyricsapplication.repository.AppRepository
import shvyn22.lyricsapplication.data.remote.Track
import shvyn22.lyricsapplication.util.StateEvent

class SearchViewModel @ViewModelInject constructor(
    private val repository: AppRepository,
    @Assisted val state: SavedStateHandle
) : ViewModel() {

    private val searchQuery = state.getLiveData<String?>("searchQuery", null)

    val tracks = searchQuery.switchMap { query ->
        liveData {
            repository.searchTracks(query).collect {
                emit(it)
            }
        }
    }

    private val searchEventChannel = Channel<StateEvent>()
    val searchEvent = searchEventChannel.receiveAsFlow()

    fun searchTracks(query: String?) = viewModelScope.launch {
        searchQuery.value = query
    }

    fun onTrackSelected(item: Track) = viewModelScope.launch {
        searchEventChannel.send(StateEvent.Loading)
        val track = if (item.hasLyrics) {
            repository.getTrack(item.idArtist, item.idAlbum, item.idTrack)
        } else item
        track.hasLyrics = item.hasLyrics
        searchEventChannel.send(StateEvent.NavigateToDetails(track))
    }

    fun onErrorOccurred() = viewModelScope.launch {
        searchEventChannel.send(StateEvent.Error)
    }
}