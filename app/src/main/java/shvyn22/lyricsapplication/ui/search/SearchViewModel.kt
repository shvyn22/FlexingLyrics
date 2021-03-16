package shvyn22.lyricsapplication.ui.search

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import shvyn22.lyricsapplication.data.AppRepository
import shvyn22.lyricsapplication.data.model.Track
import shvyn22.lyricsapplication.util.Resource

class SearchViewModel @ViewModelInject constructor(
    private val repository: AppRepository
) : ViewModel() {

    private val _tracks = MutableLiveData<Resource<List<Track>>>()
    val tracks : LiveData<Resource<List<Track>>> get() = _tracks

    val isLoading = MutableLiveData<Boolean>()

    private val searchEventChannel = Channel<SearchEvent>()
    val searchEvent = searchEventChannel.receiveAsFlow()

    fun searchTracks(query: String) = viewModelScope.launch {
        _tracks.postValue(Resource.Loading())
        _tracks.value = repository.searchTracks(query)
    }

    fun onTrackSelected(item: Track) = viewModelScope.launch {
        isLoading.postValue(true)
        val track = if (item.hasLyrics) {
            repository.getTrack(item.idArtist, item.idAlbum, item.idTrack)
        } else item
        track.hasLyrics = item.hasLyrics
        isLoading.postValue(false)
        searchEventChannel.send(SearchEvent.NavigateToDetails(track))
    }

    sealed class SearchEvent {
        data class NavigateToDetails(val track: Track) : SearchEvent()
    }
}