package shvyn22.flexinglyrics.repository.local

import kotlinx.coroutines.flow.Flow
import shvyn22.flexinglyrics.data.local.dao.HistoryDao
import shvyn22.flexinglyrics.data.local.model.HistoryItem
import shvyn22.flexinglyrics.data.remote.Track
import shvyn22.flexinglyrics.util.fromTrackToHistoryItem

class HistoryRepositoryImpl(
    private val historyDao: HistoryDao
) : HistoryRepository {

    override fun getItems(): Flow<List<HistoryItem>> = historyDao.getAll()

    override suspend fun add(track: Track) {
        fromTrackToHistoryItem(track).also {
            remove(it.idTrack)
            historyDao.insert(it)
        }
    }

    override suspend fun remove(id: Int) = historyDao.delete(id)

    override suspend fun removeAll() = historyDao.deleteAll()
}