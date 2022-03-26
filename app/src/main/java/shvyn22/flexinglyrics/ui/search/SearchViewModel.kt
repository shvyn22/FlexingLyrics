package shvyn22.flexinglyrics.ui.search

import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import shvyn22.flexinglyrics.data.remote.Track
import shvyn22.flexinglyrics.repository.remote.RemoteRepository
import shvyn22.flexinglyrics.util.Resource
import shvyn22.flexinglyrics.util.StateError
import shvyn22.flexinglyrics.util.StateEvent
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val remoteRepository: RemoteRepository
) : ViewModel() {

    private val searchQuery = MutableLiveData<String?>()

    val tracks = searchQuery.switchMap { query ->
        liveData {
            remoteRepository.searchTracks(query).collect {
                emit(it)
            }
        }
    }

    private val searchEventChannel = Channel<StateEvent>()
    val searchEvent = searchEventChannel.receiveAsFlow()

    private fun onErrorOccurred() = viewModelScope.launch {
        searchEventChannel.send(StateEvent.Error(StateError.ERROR_FETCHING_DATA))
    }

    fun searchTracks(query: String?) = viewModelScope.launch {
        searchQuery.value = query
    }

    fun onTrackSelected(item: Track) = viewModelScope.launch {
        if (!item.hasLyrics) {
            searchEventChannel.send(StateEvent.NavigateToDetails(item))
        } else {
            remoteRepository.getTrack(item.idArtist, item.idAlbum, item.idTrack).collect {
                when (it) {
                    is Resource.Success -> searchEventChannel
                        .send(StateEvent.NavigateToDetails(it.data.copy(hasLyrics = true)))
                    is Resource.Loading -> searchEventChannel.send(StateEvent.Loading)
                    is Resource.Error -> onErrorOccurred()
                    else -> Unit
                }
            }
        }
    }
}