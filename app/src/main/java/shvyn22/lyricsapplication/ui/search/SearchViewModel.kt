package shvyn22.lyricsapplication.ui.search

import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import shvyn22.lyricsapplication.data.remote.Track
import shvyn22.lyricsapplication.repository.Repository
import shvyn22.lyricsapplication.util.StateEvent
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {

    private val searchQuery = MutableLiveData<String?>()

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