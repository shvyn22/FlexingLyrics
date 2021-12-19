package shvyn22.flexinglyrics.repository

import shvyn22.flexinglyrics.data.local.dao.FakeLibraryDao
import shvyn22.flexinglyrics.data.local.dao.LibraryDao
import shvyn22.flexinglyrics.data.remote.Track
import shvyn22.flexinglyrics.repository.local.LibraryRepository
import shvyn22.flexinglyrics.util.fromTrackToLibraryItem

class FakeLibraryRepositoryImpl(
    private val libraryDao: LibraryDao = FakeLibraryDao()
): LibraryRepository {
    override fun getItems(query: String) = libraryDao.getAll(query)

    override fun isLibraryItem(id: Int) = libraryDao.exists(id)

    override suspend fun add(track: Track) {
        fromTrackToLibraryItem(track).also {
            libraryDao.insert(it)
        }
    }

    override suspend fun remove(id: Int) = libraryDao.delete(id)

    override suspend fun removeAll() = libraryDao.deleteAll()
}