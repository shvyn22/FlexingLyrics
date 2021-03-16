package shvyn22.lyricsapplication.ui.history

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
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

    private val historyFlow = repository.getHistoryItems()
    val items = historyFlow.asLiveData()

    private val historyEventChannel = Channel<HistoryEvent>()
    val historyEvent = historyEventChannel.receiveAsFlow()

    fun onTrackSelected(item: HistoryItem) = viewModelScope.launch {
        val track = if (item.hasLyrics) {
            repository.getTrack(item.idArtist, item.idAlbum, item.idTrack)
        } else {
            mapper.fromHistoryItemToTrack(item)
        }
        track.hasLyrics = item.hasLyrics
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