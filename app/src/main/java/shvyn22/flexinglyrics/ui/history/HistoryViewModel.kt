package shvyn22.flexinglyrics.ui.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import shvyn22.flexinglyrics.data.local.model.HistoryItem
import shvyn22.flexinglyrics.repository.local.HistoryRepository
import shvyn22.flexinglyrics.repository.remote.RemoteRepository
import shvyn22.flexinglyrics.util.Resource
import shvyn22.flexinglyrics.util.StateEvent
import shvyn22.flexinglyrics.util.fromHistoryItemToTrack
import javax.inject.Inject

class HistoryViewModel @Inject constructor(
    private val remoteRepository: RemoteRepository,
    private val historyRepository: HistoryRepository
) : ViewModel() {

    val items = liveData {
        historyRepository.getItems().collect {
            emit(it)
        }
    }

    private val historyEventChannel = Channel<StateEvent>()
    val historyEvent = historyEventChannel.receiveAsFlow()

    private fun onErrorOccurred() = viewModelScope.launch {
        historyEventChannel.send(StateEvent.Error)
    }

    fun onTrackSelected(item: HistoryItem) = viewModelScope.launch {
        if (!item.hasLyrics) {
            fromHistoryItemToTrack(item).also {
                historyEventChannel.send(StateEvent.NavigateToDetails(it))
            }
        } else {
            remoteRepository.getTrack(item.idArtist, item.idAlbum, item.idTrack).collect {
                when (it) {
                    is Resource.Success -> historyEventChannel
                        .send(StateEvent.NavigateToDetails(it.data.copy(hasLyrics = true)))
                    is Resource.Loading -> historyEventChannel.send(StateEvent.Loading)
                    is Resource.Error -> onErrorOccurred()
                    else -> Unit
                }
            }
        }
    }

    fun deleteTrack(id: Int) = viewModelScope.launch {
        historyRepository.remove(id)
    }

    fun deleteAll() = viewModelScope.launch {
        historyRepository.removeAll()
    }
}