package shvyn22.lyricsapplication.data.local.dao

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import shvyn22.lyricsapplication.data.local.model.HistoryItem

class FakeHistoryDao : HistoryDao {

    private val items = mutableListOf<HistoryItem>()

    override fun getAll(): Flow<List<HistoryItem>> = flow {
        emit(items.reversed())
    }

    override suspend fun insert(historyItem: HistoryItem) {
        if (items.contains(historyItem)) items.remove(historyItem)
        items.add(historyItem)
    }

    override suspend fun delete(id: Int) {
        items.removeIf { it.idTrack == id }
    }

    override suspend fun deleteAll() {
        items.clear()
    }
}