package shvyn22.flexinglyrics.repository.local

import io.reactivex.rxjava3.core.Observable
import shvyn22.flexinglyrics.data.local.model.LibraryItem

interface LibraryRepository : LocalRepository {

    fun getItems(query: String): Observable<List<LibraryItem>>

    fun isLibraryItem(id: Int): Observable<Boolean>
}