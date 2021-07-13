package shvyn22.lyricsapplication.ui.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import shvyn22.lyricsapplication.data.remote.AlbumInfo
import shvyn22.lyricsapplication.data.remote.Track
import shvyn22.lyricsapplication.repository.Repository
import shvyn22.lyricsapplication.util.StateEvent
import shvyn22.lyricsapplication.util.fromTrackInfoToTrack
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {

    var track: Track? = null
    fun init(track: Track) {
        this.track = track
        addToHistory()
    }

    private val detailsEventChannel = Channel<StateEvent>()
    val detailsEvent = detailsEventChannel.receiveAsFlow()

    val artistInfo = liveData {
        repository.getArtistInfo(track?.idArtist).collect {
            emit(it)
        }
    }

    val albumInfo = liveData {
        repository.getAlbumInfo(track?.idArtist, track?.idAlbum).collect {
            emit(it)
        }
    }

    fun isLibraryItem(id: Int) = liveData {
        repository.isLibraryItem(id).collect {
            emit(it)
        }
    }

    fun addToLibrary() = viewModelScope.launch {
        track?.let { track ->
            repository.addToLibrary(track)
        }
    }

    fun removeFromLibrary() = viewModelScope.launch {
        track?.let { track ->
            repository.deleteLibraryItem(track.idTrack)
        }
    }

    private fun addToHistory() = viewModelScope.launch {
        track?.let { track ->
            repository.addToHistory(track)
        }
    }

    fun onMediaIconClick(url: String) = viewModelScope.launch {
        detailsEventChannel.send(StateEvent.NavigateToMedia(url))
    }

    fun onItemClick(item: AlbumInfo.TrackInfo) = viewModelScope.launch {
        if (track == null) detailsEventChannel.send(StateEvent.Error)
        track?.let { track ->
            val transitionTrack = if (item.hasLyrics) {
                repository.getTrack(track.idArtist, track.idAlbum, item.idTrack)
            } else {
                fromTrackInfoToTrack(item, track)
            }
            transitionTrack.hasLyrics = item.hasLyrics
            detailsEventChannel.send(StateEvent.NavigateToDetails(transitionTrack))
        }
    }

    fun onErrorOccurred() = viewModelScope.launch {
        detailsEventChannel.send(StateEvent.Error)
    }

}