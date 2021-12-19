package shvyn22.flexinglyrics.repository.local

import kotlinx.coroutines.flow.Flow
import shvyn22.flexinglyrics.data.local.model.HistoryItem

interface HistoryRepository : LocalRepository {

    fun getItems(): Flow<List<HistoryItem>>
}