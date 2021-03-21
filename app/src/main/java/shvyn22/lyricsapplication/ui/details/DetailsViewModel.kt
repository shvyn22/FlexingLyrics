package shvyn22.lyricsapplication.ui.details

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import shvyn22.lyricsapplication.data.AppRepository
import shvyn22.lyricsapplication.data.model.*
import shvyn22.lyricsapplication.util.Mapper
import shvyn22.lyricsapplication.util.Resource

class DetailsViewModel @ViewModelInject constructor(
    private val repository: AppRepository,
    private val mapper: Mapper
): ViewModel() {

    var track: Track? = null
    fun init(track: Track) {
        this.track = track
    }

    private val _isLibraryItem = MutableLiveData<Boolean>()
    val isLibraryItem: LiveData<Boolean> get() = _isLibraryItem

    private val _artistInfo = MutableLiveData<Resource<ArtistInfo>>()
    val artistInfo : LiveData<Resource<ArtistInfo>> get() = _artistInfo

    private val _albumInfo = MutableLiveData<Resource<AlbumInfo>>()
    val albumInfo : LiveData<Resource<AlbumInfo>> get() = _albumInfo

    private val detailsEventChannel = Channel<DetailsEvent>()
    val detailsEvent = detailsEventChannel.receiveAsFlow()

    fun getArtistInfo(artist: Int) = viewModelScope.launch {
        _artistInfo.postValue(Resource.Loading())
        _artistInfo.value = repository.getArtistInfo(artist)
    }

    fun getAlbumInfo(artist: Int, album: Int) = viewModelScope.launch {
        _albumInfo.postValue(Resource.Loading())
        _albumInfo.value = repository.getAlbumInfo(artist, album)
    }

    fun onToggleLibrary(track: Track) {
        if (_isLibraryItem.value!!) removeFromLibrary(track.idTrack) else addToLibrary(track)
        _isLibraryItem.value = !_isLibraryItem.value!!
    }

    private fun addToLibrary(track: Track) = viewModelScope.launch {
        val newItem = mapper.fromTrackToLibraryItem(track)
        repository.addToLibrary(newItem)
    }

    private fun removeFromLibrary(id: Int) = viewModelScope.launch {
        repository.deleteLibraryItem(id)
    }

    fun addToHistory(track: Track) = viewModelScope.launch {
        val newItem = mapper.fromTrackToHistoryItem(track)
        repository.deleteHistoryItem(newItem.idTrack)
        repository.addToHistory(newItem)
    }

    fun isLibraryItem(id: Int) = viewModelScope.launch {
        _isLibraryItem.value = repository.isLibraryItem(id)
    }

    fun onMediaIconClick(url: String) = viewModelScope.launch {
        detailsEventChannel.send(DetailsEvent.NavigateToMedia(url))
    }

    fun onItemClick(item: AlbumInfo.TrackInfo) = viewModelScope.launch {
        val transitionTrack = if (item.hasLyrics) {
            repository.getTrack(track?.idArtist!!, track?.idAlbum!!, item.idTrack)
        } else {
            mapper.fromTrackInfoToTrack(item, track!!)
        }
        transitionTrack.hasLyrics = item.hasLyrics
        detailsEventChannel.send(DetailsEvent.NavigateToDetails(transitionTrack))
    }

    sealed class DetailsEvent {
        data class NavigateToDetails(val track: Track) : DetailsEvent()
        data class NavigateToMedia(val url: String) : DetailsEvent()
    }
}