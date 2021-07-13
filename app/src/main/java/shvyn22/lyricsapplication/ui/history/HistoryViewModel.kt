package shvyn22.lyricsapplication.ui.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import shvyn22.lyricsapplication.data.local.model.HistoryItem
import shvyn22.lyricsapplication.repository.Repository
import shvyn22.lyricsapplication.util.StateEvent
import shvyn22.lyricsapplication.util.fromHistoryItemToTrack
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {

    val items = liveData {
        repository.getHistoryItems().collect {
            emit(it)
        }
    }

    private val historyEventChannel = Channel<StateEvent>()
    val historyEvent = historyEventChannel.receiveAsFlow()

    fun onTrackSelected(item: HistoryItem) = viewModelScope.launch {
        historyEventChannel.send(StateEvent.Loading)
        val track = if (item.hasLyrics) {
            repository.getTrack(item.idArtist, item.idAlbum, item.idTrack)
        } else {
            fromHistoryItemToTrack(item)
        }
        track.hasLyrics = item.hasLyrics
        historyEventChannel.send(StateEvent.NavigateToDetails(track))
    }

    fun onErrorOccurred() = viewModelScope.launch {
        historyEventChannel.send(StateEvent.Error)
    }

    fun deleteTrack(id: Int) = viewModelScope.launch {
        repository.removeFromHistory(id)
    }

    fun deleteAll() = viewModelScope.launch {
        repository.deleteHistoryItems()
    }
}