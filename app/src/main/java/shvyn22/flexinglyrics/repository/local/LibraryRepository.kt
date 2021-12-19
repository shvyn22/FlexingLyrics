package shvyn22.flexinglyrics.repository.local

import kotlinx.coroutines.flow.Flow
import shvyn22.flexinglyrics.data.local.model.LibraryItem

interface LibraryRepository : LocalRepository {

    fun getItems(query: String): Flow<List<LibraryItem>>

    fun isLibraryItem(id: Int): Flow<Boolean>
}