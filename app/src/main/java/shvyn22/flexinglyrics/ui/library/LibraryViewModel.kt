package shvyn22.flexinglyrics.ui.library

import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import shvyn22.flexinglyrics.data.local.model.LibraryItem
import shvyn22.flexinglyrics.repository.local.LibraryRepository
import shvyn22.flexinglyrics.repository.remote.RemoteRepository
import shvyn22.flexinglyrics.util.Resource
import shvyn22.flexinglyrics.util.StateEvent
import shvyn22.flexinglyrics.util.fromLibraryItemToTrack
import javax.inject.Inject

@HiltViewModel
class LibraryViewModel @Inject constructor(
    private val remoteRepository: RemoteRepository,
    private val libraryRepository: LibraryRepository
) : ViewModel() {

    private val searchQuery = MutableLiveData("")

    val items = searchQuery.switchMap {
        libraryRepository.getItems(it).asLiveData()
    }

    private val libraryEventChannel = Channel<StateEvent>()
    val libraryEvent = libraryEventChannel.receiveAsFlow()

    private fun onErrorOccurred() = viewModelScope.launch {
        libraryEventChannel.send(StateEvent.Error)
    }

    fun searchTracks(query: String) {
        searchQuery.value = query
    }

    fun onTrackSelected(item: LibraryItem) = viewModelScope.launch {
        if (!item.hasLyrics) {
            fromLibraryItemToTrack(item).also {
                libraryEventChannel.send(StateEvent.NavigateToDetails(it))
            }
        } else {
            remoteRepository.getTrack(item.idArtist, item.idAlbum, item.idTrack).collect {
                when (it) {
                    is Resource.Success -> libraryEventChannel
                        .send(StateEvent.NavigateToDetails(it.data.copy(hasLyrics = true)))
                    is Resource.Loading -> libraryEventChannel.send(StateEvent.Loading)
                    is Resource.Error -> onErrorOccurred()
                    else -> Unit
                }
            }
        }
    }

    fun deleteTrack(id: Int) = viewModelScope.launch {
        libraryRepository.remove(id)
    }

    fun deleteAll() = viewModelScope.launch {
        libraryRepository.removeAll()
    }
}