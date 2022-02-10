package shvyn22.flexinglyrics.ui.library

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.PublishSubject
import shvyn22.flexinglyrics.data.local.model.LibraryItem
import shvyn22.flexinglyrics.repository.local.LibraryRepository
import shvyn22.flexinglyrics.repository.remote.RemoteRepository
import shvyn22.flexinglyrics.util.Resource
import shvyn22.flexinglyrics.util.StateEvent
import shvyn22.flexinglyrics.util.fromLibraryItemToTrack
import shvyn22.flexinglyrics.util.toLiveData
import javax.inject.Inject

class LibraryViewModel @Inject constructor(
    private val remoteRepository: RemoteRepository,
    private val libraryRepository: LibraryRepository
) : ViewModel() {

    private val searchQuery = MutableLiveData("")

    val items = searchQuery.switchMap {
        libraryRepository
            .getItems(it)
            .subscribeOn(Schedulers.io())
            .toLiveData()
    }

    private val libraryEventChannel = PublishSubject.create<StateEvent>()
    val libraryEvent = libraryEventChannel.toLiveData()

    private fun onErrorOccurred() {
        libraryEventChannel.onNext(StateEvent.Error)
    }

    fun searchTracks(query: String) {
        searchQuery.value = query
    }

    fun onTrackSelected(item: LibraryItem) {
        if (!item.hasLyrics) {
            fromLibraryItemToTrack(item).also {
                libraryEventChannel.onNext(StateEvent.NavigateToDetails(it))
            }
        } else {
            remoteRepository
                .getTrack(item.idArtist, item.idAlbum, item.idTrack)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                when (it) {
                    is Resource.Success -> libraryEventChannel
                        .onNext(StateEvent.NavigateToDetails(it.data.copy(hasLyrics = true)))
                    is Resource.Loading -> libraryEventChannel.onNext(StateEvent.Loading)
                    is Resource.Error -> onErrorOccurred()
                    else -> Unit
                }
            }
        }
    }

    fun deleteTrack(id: Int) {
        libraryRepository.remove(id)
    }

    fun deleteAll() {
        libraryRepository.removeAll()
    }
}