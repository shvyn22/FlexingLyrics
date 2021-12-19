package shvyn22.flexinglyrics.data.local.dao

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import shvyn22.flexinglyrics.data.local.model.LibraryItem

class FakeLibraryDao : LibraryDao {

    private val items = mutableListOf<LibraryItem>()

    override fun getAll(query: String): Flow<List<LibraryItem>> = flow {
        emit(items.filter { it.track.contains(query) || it.artist.contains(query) })
    }

    override fun exists(id: Int): Flow<Boolean> = flow {
        emit(items.find { it.idTrack == id } != null)
    }

    override suspend fun insert(libraryItem: LibraryItem) {
        items.add(libraryItem)
    }

    override suspend fun delete(id: Int) {
        items.removeIf { it.idTrack == id }
    }

    override suspend fun deleteAll() {
        items.clear()
    }

}