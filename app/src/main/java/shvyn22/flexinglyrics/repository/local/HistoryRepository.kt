package shvyn22.flexinglyrics.repository.local

import io.reactivex.rxjava3.core.Observable
import shvyn22.flexinglyrics.data.local.model.HistoryItem

interface HistoryRepository : LocalRepository {

    fun getItems(): Observable<List<HistoryItem>>
}