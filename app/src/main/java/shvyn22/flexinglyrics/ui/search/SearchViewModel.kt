package shvyn22.flexinglyrics.ui.search

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.PublishSubject
import shvyn22.flexinglyrics.data.remote.Track
import shvyn22.flexinglyrics.repository.remote.RemoteRepository
import shvyn22.flexinglyrics.util.Resource
import shvyn22.flexinglyrics.util.StateEvent
import shvyn22.flexinglyrics.util.toLiveData
import javax.inject.Inject

class SearchViewModel @Inject constructor(
    private val remoteRepository: RemoteRepository
) : ViewModel() {

    private val searchQuery = MutableLiveData<String?>()

    val tracks = searchQuery.switchMap { query ->
        remoteRepository
            .searchTracks(query)
            .subscribeOn(Schedulers.io())
            .toLiveData()
    }

    private val searchEventChannel = PublishSubject.create<StateEvent>()
    val searchEvent = searchEventChannel.toLiveData()

    private fun onErrorOccurred() {
        searchEventChannel.onNext(StateEvent.Error)
    }

    fun searchTracks(query: String?) {
        searchQuery.value = query
    }

    fun onTrackSelected(item: Track) {
        if (!item.hasLyrics) {
            searchEventChannel.onNext(StateEvent.NavigateToDetails(item))
        } else {
            remoteRepository
                .getTrack(item.idArtist, item.idAlbum, item.idTrack)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    when (it) {
                        is Resource.Success -> searchEventChannel
                            .onNext(StateEvent.NavigateToDetails(it.data.copy(hasLyrics = true)))
                        is Resource.Loading -> searchEventChannel.onNext(StateEvent.Loading)
                        is Resource.Error -> onErrorOccurred()
                        else -> Unit
                    }
                }
        }
    }
}