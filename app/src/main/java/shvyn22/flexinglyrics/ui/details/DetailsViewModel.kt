package shvyn22.flexinglyrics.ui.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import shvyn22.flexinglyrics.data.remote.AlbumInfo
import shvyn22.flexinglyrics.data.remote.Track
import shvyn22.flexinglyrics.repository.local.HistoryRepository
import shvyn22.flexinglyrics.repository.local.LibraryRepository
import shvyn22.flexinglyrics.repository.remote.RemoteRepository
import shvyn22.flexinglyrics.util.Resource
import shvyn22.flexinglyrics.util.StateError
import shvyn22.flexinglyrics.util.StateEvent
import shvyn22.flexinglyrics.util.fromTrackInfoToTrack
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val remoteRepository: RemoteRepository,
    private val libraryRepository: LibraryRepository,
    private val historyRepository: HistoryRepository
) : ViewModel() {

    private var _track: Track? = null
    val track: Track get() = _track!!

    fun init(track: Track) {
        this._track = track
        addToHistory()
    }

    private val detailsEventChannel = Channel<StateEvent>()
    val detailsEvent = detailsEventChannel.receiveAsFlow()

    val artistInfo = liveData {
        remoteRepository.getArtistInfo(track.idArtist).collect {
            emit(it)
        }
    }

    val albumInfo = liveData {
        remoteRepository.getAlbumInfo(track.idArtist, track.idAlbum).collect {
            emit(it)
        }
    }

    fun isLibraryItem(id: Int) = liveData {
        libraryRepository.isLibraryItem(id).collect {
            emit(it)
        }
    }

    fun addToLibrary() = viewModelScope.launch {
        libraryRepository.add(track)
    }

    fun removeFromLibrary() = viewModelScope.launch {
        libraryRepository.remove(track.idTrack)
    }

    private fun addToHistory() = viewModelScope.launch {
        historyRepository.add(track)
    }

    fun onMediaIconClick(url: String) = viewModelScope.launch {
        detailsEventChannel.send(StateEvent.NavigateToMedia(url))
    }

    fun onItemClick(item: AlbumInfo.TrackInfo) = viewModelScope.launch {
        if (!item.hasLyrics) {
            fromTrackInfoToTrack(item, track).also {
                detailsEventChannel.send(StateEvent.NavigateToDetails(it))
            }
        }
        else {
            remoteRepository.getTrack(track.idArtist, track.idAlbum, item.idTrack).collect {
                when (it) {
                    is Resource.Success -> detailsEventChannel
                        .send(StateEvent.NavigateToDetails(it.data.copy(hasLyrics = true)))
                    is Resource.Loading -> detailsEventChannel.send(StateEvent.Loading)
                    is Resource.Error -> onErrorOccurred(StateError.ERROR_FETCHING_DATA)
                    else -> Unit
                }
            }
        }
    }

    fun onErrorOccurred(error: StateError) = viewModelScope.launch {
        detailsEventChannel.send(StateEvent.Error(error))
    }
}