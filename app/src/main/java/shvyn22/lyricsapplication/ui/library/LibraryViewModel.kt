package shvyn22.lyricsapplication.ui.library

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import shvyn22.lyricsapplication.data.AppRepository
import shvyn22.lyricsapplication.data.model.LibraryItem
import shvyn22.lyricsapplication.data.model.Track
import shvyn22.lyricsapplication.util.Mapper

class LibraryViewModel @ViewModelInject constructor(
    private val repository: AppRepository,
    private val mapper: Mapper,
    @Assisted val state: SavedStateHandle
) : ViewModel() {

    private val searchQuery = state.getLiveData("librarySearch", "")

    val items = searchQuery.switchMap {
        repository.getLibraryItems(it).asLiveData()
    }

    private val libraryEventChannel = Channel<LibraryEvent>()
    val libraryEvent = libraryEventChannel.receiveAsFlow()

    fun searchTracks(query: String) {
        searchQuery.value = query
    }

    fun onTrackSelected(item: LibraryItem) = viewModelScope.launch {
        val track = if (item.hasLyrics) {
            repository.getTrack(item.idArtist, item.idAlbum, item.idTrack)
        } else {
            mapper.fromLibraryItemToTrack(item)
        }
        track.hasLyrics = item.hasLyrics
        libraryEventChannel.send(LibraryEvent.NavigateToDetails(track))
    }

    fun deleteTrack(id: Int) = viewModelScope.launch {
        repository.deleteLibraryItem(id)
    }

    fun deleteAll() = viewModelScope.launch {
        repository.deleteLibraryItems()
    }

    sealed class LibraryEvent {
        data class NavigateToDetails(val track: Track) : LibraryEvent()
    }
}