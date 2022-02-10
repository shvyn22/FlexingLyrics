package shvyn22.flexinglyrics.repository.local

import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import shvyn22.flexinglyrics.data.local.dao.LibraryDao
import shvyn22.flexinglyrics.data.local.model.LibraryItem
import shvyn22.flexinglyrics.data.remote.Track
import shvyn22.flexinglyrics.util.fromTrackToLibraryItem

class LibraryRepositoryImpl(
    private val libraryDao: LibraryDao
) : LibraryRepository {

    override fun getItems(query: String): Observable<List<LibraryItem>> = libraryDao.getAll(query)

    override fun isLibraryItem(id: Int): Observable<Boolean> = libraryDao.exists(id)

    override fun add(track: Track) {
        fromTrackToLibraryItem(track).also {
            libraryDao
                .insert(it)
                .subscribeOn(Schedulers.io())
                .subscribe()
        }
    }

    override fun remove(id: Int) {
        libraryDao
            .delete(id)
            .subscribeOn(Schedulers.io())
            .subscribe()
    }

    override fun removeAll() {
        libraryDao
            .deleteAll()
            .subscribeOn(Schedulers.io())
            .subscribe()
    }
}