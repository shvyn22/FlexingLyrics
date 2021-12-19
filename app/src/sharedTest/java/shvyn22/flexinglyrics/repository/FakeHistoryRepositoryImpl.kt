package shvyn22.flexinglyrics.repository

import shvyn22.flexinglyrics.data.local.dao.FakeHistoryDao
import shvyn22.flexinglyrics.data.local.dao.HistoryDao
import shvyn22.flexinglyrics.data.remote.Track
import shvyn22.flexinglyrics.repository.local.HistoryRepository
import shvyn22.flexinglyrics.util.fromTrackToHistoryItem

class FakeHistoryRepositoryImpl(
    private val historyDao: HistoryDao = FakeHistoryDao()
): HistoryRepository {
    override fun getItems() = historyDao.getAll()

    override suspend fun add(track: Track) {
        fromTrackToHistoryItem(track).also {
            remove(it.idTrack)
            historyDao.insert(it)
        }
    }

    override suspend fun remove(id: Int) = historyDao.delete(id)

    override suspend fun removeAll() = historyDao.deleteAll()
}