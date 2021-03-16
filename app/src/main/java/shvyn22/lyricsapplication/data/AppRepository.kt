package shvyn22.lyricsapplication.data

import shvyn22.lyricsapplication.LyricsApplication.Companion.ERROR_FETCHING_DATA
import shvyn22.lyricsapplication.api.ApiInterface
import shvyn22.lyricsapplication.data.dao.HistoryDao
import shvyn22.lyricsapplication.data.dao.LibraryDao
import shvyn22.lyricsapplication.data.model.*
import shvyn22.lyricsapplication.util.Resource
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppRepository @Inject constructor(
    private val api: ApiInterface,
    private val historyDao: HistoryDao,
    private val libraryDao: LibraryDao
){
    suspend fun searchTracks(query: String) : Resource<List<Track>> {
        val response = api.searchTracks(query)

        if (response.success) return Resource.Success(response.result)
        return Resource.Error(ERROR_FETCHING_DATA)
    }

    suspend fun getArtistInfo(artistId: Int) : Resource<ArtistInfo> {
        val response = api.getArtistInfo(artistId)

        if (response.success) return Resource.Success(response.result)
        return Resource.Error(ERROR_FETCHING_DATA)
    }

    suspend fun getAlbumInfo(artistId: Int, albumId: Int) : Resource<AlbumInfo> {
        val response = api.getAlbumInfo(artistId, albumId)

        if (response.success) return Resource.Success(response.result)
        return Resource.Error(ERROR_FETCHING_DATA)
    }

    suspend fun getTrack(artistId: Int, albumId: Int, trackId: Int) : Track {
        return api.getTrackInfo(artistId, albumId, trackId).result
    }

    fun getHistoryItems() = historyDao.getAll()

    suspend fun addToHistory(historyItem: HistoryItem) = historyDao.insert(historyItem)

    suspend fun deleteHistoryItem(id: Int) = historyDao.delete(id)

    suspend fun deleteHistoryItems() = historyDao.deleteAll()

    fun getLibraryItems(query: String) = libraryDao.getAll(query)

    suspend fun isLibraryItem(id: Int) = libraryDao.exists(id)

    suspend fun addToLibrary(libraryItem: LibraryItem) = libraryDao.insert(libraryItem)

    suspend fun deleteLibraryItem(id: Int) = libraryDao.delete(id)

    suspend fun deleteLibraryItems() = libraryDao.deleteAll()
}