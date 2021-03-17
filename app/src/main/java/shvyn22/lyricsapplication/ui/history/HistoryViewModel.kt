package shvyn22.lyricsapplication.ui.history

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import shvyn22.lyricsapplication.data.AppRepository
import shvyn22.lyricsapplication.data.model.HistoryItem
import shvyn22.lyricsapplication.data.model.Track
import shvyn22.lyricsapplication.util.Mapper

class HistoryViewModel @ViewModelInject constructor(
    private val repository: AppRepository,
    private val mapper: Mapper
) : ViewModel() {

    private val _items = MutableLiveData<List<HistoryItem>>()
    val items : LiveData<List<HistoryItem>> get() = _items

    val isLoading = MutableLiveData<Boolean>()

    private val historyEventChannel = Channel<HistoryEvent>()
    val historyEvent = historyEventChannel.receiveAsFlow()

    fun getHistoryItems() = viewModelScope.launch {
        repository.getHistoryItems().collect {
            _items.value = it
        }
    }

    fun onTrackSelected(item: HistoryItem) = viewModelScope.launch {
        isLoading.postValue(true)
        val track = if (item.hasLyrics) {
            repository.getTrack(item.idArtist, item.idAlbum, item.idTrack)
        } else {
            mapper.fromHistoryItemToTrack(item)
        }
        track.hasLyrics = item.hasLyrics
        isLoading.postValue(false)
        historyEventChannel.send(HistoryEvent.NavigateToDetails(track))
    }

    fun deleteTrack(id: Int) = viewModelScope.launch {
        repository.deleteHistoryItem(id)
    }

    fun deleteAll() = viewModelScope.launch {
        repository.deleteHistoryItems()
    }

    sealed class HistoryEvent {
        data class NavigateToDetails(val track: Track) : HistoryEvent()
    }
}