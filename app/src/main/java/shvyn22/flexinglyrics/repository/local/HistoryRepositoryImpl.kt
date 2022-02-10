package shvyn22.flexinglyrics.repository.local

import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import shvyn22.flexinglyrics.data.local.dao.HistoryDao
import shvyn22.flexinglyrics.data.local.model.HistoryItem
import shvyn22.flexinglyrics.data.remote.Track
import shvyn22.flexinglyrics.util.fromTrackToHistoryItem

class HistoryRepositoryImpl(
    private val historyDao: HistoryDao
) : HistoryRepository {

    override fun getItems(): Observable<List<HistoryItem>> = historyDao.getAll()

    override fun add(track: Track) {
        fromTrackToHistoryItem(track).also {
            remove(it.idTrack)
            historyDao
                .insert(it)
                .subscribeOn(Schedulers.io())
                .subscribe()
        }
    }

    override fun remove(id: Int) {
        historyDao
            .delete(id)
            .subscribeOn(Schedulers.io())
            .subscribe()
    }

    override fun removeAll() {
        historyDao
            .deleteAll()
            .subscribeOn(Schedulers.io())
            .subscribe()
    }
}