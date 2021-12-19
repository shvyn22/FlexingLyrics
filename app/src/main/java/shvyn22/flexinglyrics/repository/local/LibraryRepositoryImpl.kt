package shvyn22.flexinglyrics.repository.local

import kotlinx.coroutines.flow.Flow
import shvyn22.flexinglyrics.data.local.dao.LibraryDao
import shvyn22.flexinglyrics.data.local.model.LibraryItem
import shvyn22.flexinglyrics.data.remote.Track
import shvyn22.flexinglyrics.util.fromTrackToLibraryItem

class LibraryRepositoryImpl(
    private val libraryDao: LibraryDao
) : LibraryRepository {

    override fun getItems(query: String): Flow<List<LibraryItem>> = libraryDao.getAll(query)

    override fun isLibraryItem(id: Int): Flow<Boolean> = libraryDao.exists(id)

    override suspend fun add(track: Track) {
        fromTrackToLibraryItem(track).also {
            libraryDao.insert(it)
        }
    }

    override suspend fun remove(id: Int) = libraryDao.delete(id)

    override suspend fun removeAll() = libraryDao.deleteAll()
}