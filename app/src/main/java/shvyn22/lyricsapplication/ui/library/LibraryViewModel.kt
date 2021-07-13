package shvyn22.lyricsapplication.ui.library

import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import shvyn22.lyricsapplication.data.local.model.LibraryItem
import shvyn22.lyricsapplication.repository.Repository
import shvyn22.lyricsapplication.util.StateEvent
import shvyn22.lyricsapplication.util.fromLibraryItemToTrack
import javax.inject.Inject

@HiltViewModel
class LibraryViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {

    private val searchQuery = MutableLiveData("")

    val items = searchQuery.switchMap {
        repository.getLibraryItems(it).asLiveData()
    }

    private val libraryEventChannel = Channel<StateEvent>()
    val libraryEvent = libraryEventChannel.receiveAsFlow()

    fun searchTracks(query: String) {
        searchQuery.value = query
    }

    fun onTrackSelected(item: LibraryItem) = viewModelScope.launch {
        libraryEventChannel.send(StateEvent.Loading)
        val track = if (item.hasLyrics) {
            repository.getTrack(item.idArtist, item.idAlbum, item.idTrack)
        } else {
            fromLibraryItemToTrack(item)
        }
        track.hasLyrics = item.hasLyrics
        libraryEventChannel.send(StateEvent.NavigateToDetails(track))
    }

    fun onErrorOccurred() = viewModelScope.launch {
        libraryEventChannel.send(StateEvent.Error)
    }

    fun deleteTrack(id: Int) = viewModelScope.launch {
        repository.deleteLibraryItem(id)
    }

    fun deleteAll() = viewModelScope.launch {
        repository.deleteLibraryItems()
    }
}