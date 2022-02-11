package shvyn22.flexinglyrics.ui.history

import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.PublishSubject
import shvyn22.flexinglyrics.data.local.model.HistoryItem
import shvyn22.flexinglyrics.repository.local.HistoryRepository
import shvyn22.flexinglyrics.repository.remote.RemoteRepository
import shvyn22.flexinglyrics.util.Resource
import shvyn22.flexinglyrics.util.StateEvent
import shvyn22.flexinglyrics.util.fromHistoryItemToTrack
import shvyn22.flexinglyrics.util.toLiveData
import javax.inject.Inject

class HistoryViewModel @Inject constructor(
    private val remoteRepository: RemoteRepository,
    private val historyRepository: HistoryRepository
) : ViewModel() {

    val items =
        historyRepository
            .getItems()
            .subscribeOn(Schedulers.io())
            .toLiveData()

    private val historyEventChannel = PublishSubject.create<StateEvent>()
    val historyEvent: Observable<StateEvent> = historyEventChannel.flatMap { Observable.just(it) }

    private fun onErrorOccurred() {
        historyEventChannel.onNext(StateEvent.Error)
    }

    fun onTrackSelected(item: HistoryItem) {
        if (!item.hasLyrics) {
            fromHistoryItemToTrack(item).also {
                historyEventChannel.onNext(StateEvent.NavigateToDetails(it))
            }
        } else {
            remoteRepository
                .getTrack(item.idArtist, item.idAlbum, item.idTrack)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    when (it) {
                        is Resource.Success -> historyEventChannel
                            .onNext(StateEvent.NavigateToDetails(it.data.copy(hasLyrics = true)))
                        is Resource.Loading -> historyEventChannel.onNext(StateEvent.Loading)
                        is Resource.Error -> onErrorOccurred()
                        else -> Unit
                    }
                }
        }
    }

    fun deleteTrack(id: Int) {
        historyRepository.remove(id)
    }

    fun deleteAll() {
        historyRepository.removeAll()
    }
}