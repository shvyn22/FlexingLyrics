package shvyn22.lyricsapplication.repository

import kotlinx.coroutines.flow.flow
import shvyn22.lyricsapplication.LyricsApplication.Companion.ERROR_FETCHING_DATA
import shvyn22.lyricsapplication.api.ApiInterface
import shvyn22.lyricsapplication.data.local.dao.HistoryDao
import shvyn22.lyricsapplication.data.local.dao.LibraryDao
import shvyn22.lyricsapplication.data.local.model.*
import shvyn22.lyricsapplication.data.remote.AlbumInfo
import shvyn22.lyricsapplication.data.remote.ArtistInfo
import shvyn22.lyricsapplication.data.remote.Track
import shvyn22.lyricsapplication.util.Resource
import shvyn22.lyricsapplication.util.fromTrackToHistoryItem
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppRepository @Inject constructor(
    private val api: ApiInterface,
    private val historyDao: HistoryDao,
    private val libraryDao: LibraryDao
) {
    suspend fun searchTracks(query: String?) = flow<Resource<List<Track>>> {
        emit(Resource.Loading())
        if (query == null) emit(Resource.Idle())
        else {
            val response = api.searchTracks(query)

            if (response.success) emit(Resource.Success(response.result))
            else emit(Resource.Error(ERROR_FETCHING_DATA))
        }
    }

    suspend fun getArtistInfo(artistId: Int?) = flow<Resource<ArtistInfo>> {
        if (artistId == null) emit(Resource.Error(ERROR_FETCHING_DATA))
        else {
            emit(Resource.Loading())
            val response = api.getArtistInfo(artistId)

            if (response.success) emit(Resource.Success(response.result))
            else emit(Resource.Error(ERROR_FETCHING_DATA))
        }
    }

    suspend fun getAlbumInfo(artistId: Int?, albumId: Int?) = flow<Resource<AlbumInfo>> {
        if (artistId == null || albumId == null)
            emit(Resource.Error(ERROR_FETCHING_DATA))
        else {
            emit(Resource.Loading())
            val response = api.getAlbumInfo(artistId, albumId)

            if (response.success) emit(Resource.Success(response.result))
            else emit(Resource.Error(ERROR_FETCHING_DATA))
        }
    }

    suspend fun getTrack(artistId: Int, albumId: Int, trackId: Int): Track {
        return api.getTrackInfo(artistId, albumId, trackId).result
    }

    fun getHistoryItems() = historyDao.getAll()

    suspend fun addToHistory(track: Track) {
        val newItem = fromTrackToHistoryItem(track)
        removeFromHistory(newItem.idTrack)
        historyDao.insert(newItem)
    }

    suspend fun removeFromHistory(id: Int) = historyDao.delete(id)

    suspend fun deleteHistoryItems() = historyDao.deleteAll()

    fun getLibraryItems(query: String) = libraryDao.getAll(query)

    fun isLibraryItem(id: Int) = libraryDao.exists(id)

    suspend fun addToLibrary(libraryItem: LibraryItem) = libraryDao.insert(libraryItem)

    suspend fun deleteLibraryItem(id: Int) = libraryDao.delete(id)

    suspend fun deleteLibraryItems() = libraryDao.deleteAll()
}