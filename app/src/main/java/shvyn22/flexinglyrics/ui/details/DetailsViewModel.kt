package shvyn22.flexinglyrics.ui.details

import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.PublishSubject
import shvyn22.flexinglyrics.data.remote.AlbumInfo
import shvyn22.flexinglyrics.data.remote.Track
import shvyn22.flexinglyrics.repository.local.HistoryRepository
import shvyn22.flexinglyrics.repository.local.LibraryRepository
import shvyn22.flexinglyrics.repository.remote.RemoteRepository
import shvyn22.flexinglyrics.util.Resource
import shvyn22.flexinglyrics.util.StateEvent
import shvyn22.flexinglyrics.util.fromTrackInfoToTrack
import shvyn22.flexinglyrics.util.toLiveData
import javax.inject.Inject

class DetailsViewModel @Inject constructor(
    private val remoteRepository: RemoteRepository,
    private val libraryRepository: LibraryRepository,
    private val historyRepository: HistoryRepository
) : ViewModel() {

    private var _track: Track? = null
    val track: Track get() = _track!!

    fun init(track: Track) {
        this._track = track
        addToHistory()
    }

    private val detailsEventChannel = PublishSubject.create<StateEvent>()
    val detailsEvent = detailsEventChannel.toLiveData()

    val artistInfo =
        remoteRepository
            .getArtistInfo(track.idArtist)
            .subscribeOn(Schedulers.io())
            .toLiveData()

    val albumInfo =
        remoteRepository
            .getAlbumInfo(track.idArtist, track.idAlbum)
            .subscribeOn(Schedulers.io())
            .toLiveData()

    fun isLibraryItem(id: Int) =
        libraryRepository
            .isLibraryItem(id)
            .subscribeOn(Schedulers.io())
            .toLiveData()

    fun addToLibrary() {
        libraryRepository.add(track)
    }

    fun removeFromLibrary() {
        libraryRepository.remove(track.idTrack)
    }

    private fun addToHistory() {
        historyRepository.add(track)
    }

    fun onMediaIconClick(url: String) {
        detailsEventChannel.onNext(StateEvent.NavigateToMedia(url))
    }

    fun onItemClick(item: AlbumInfo.TrackInfo) {
        if (!item.hasLyrics) {
            fromTrackInfoToTrack(item, track).also {
                detailsEventChannel.onNext(StateEvent.NavigateToDetails(it))
            }
        } else {
            remoteRepository
                .getTrack(track.idArtist, track.idAlbum, item.idTrack)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    when (it) {
                        is Resource.Success -> detailsEventChannel
                            .onNext(StateEvent.NavigateToDetails(it.data.copy(hasLyrics = true)))
                        is Resource.Loading -> detailsEventChannel.onNext(StateEvent.Loading)
                        is Resource.Error -> onErrorOccurred()
                        else -> Unit
                    }
                }
        }
    }

    fun onErrorOccurred() {
        detailsEventChannel.onNext(StateEvent.Error)
    }
}