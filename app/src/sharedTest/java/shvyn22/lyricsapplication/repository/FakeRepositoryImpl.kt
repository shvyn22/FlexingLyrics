package shvyn22.lyricsapplication.repository

import kotlinx.coroutines.flow.flow
import shvyn22.lyricsapplication.LyricsApplication
import shvyn22.lyricsapplication.api.ApiInterface
import shvyn22.lyricsapplication.api.FakeApiImpl
import shvyn22.lyricsapplication.data.local.dao.FakeHistoryDao
import shvyn22.lyricsapplication.data.local.dao.FakeLibraryDao
import shvyn22.lyricsapplication.data.local.dao.HistoryDao
import shvyn22.lyricsapplication.data.local.dao.LibraryDao
import shvyn22.lyricsapplication.data.remote.AlbumInfo
import shvyn22.lyricsapplication.data.remote.ArtistInfo
import shvyn22.lyricsapplication.data.remote.Track
import shvyn22.lyricsapplication.util.Resource
import shvyn22.lyricsapplication.util.fromTrackToHistoryItem
import shvyn22.lyricsapplication.util.fromTrackToLibraryItem

class FakeRepositoryImpl(
    private val api: ApiInterface = FakeApiImpl(),
    private val historyDao: HistoryDao = FakeHistoryDao(),
    private val libraryDao: LibraryDao = FakeLibraryDao()
) : Repository {

    override suspend fun searchTracks(query: String?) = flow<Resource<List<Track>>> {
        if (query == null) emit(Resource.Idle())
        else {
            val response = api.searchTracks(query)

            if (response.success) emit(Resource.Success(response.result))
            else emit(Resource.Error(LyricsApplication.ERROR_FETCHING_DATA))
        }
    }

    override suspend fun getArtistInfo(artistId: Int?) = flow<Resource<ArtistInfo>> {
        if (artistId == null) emit(Resource.Error(LyricsApplication.ERROR_FETCHING_DATA))
        else {
            val response = api.getArtistInfo(artistId)

            if (response.success) emit(Resource.Success(response.result))
            else emit(Resource.Error(LyricsApplication.ERROR_FETCHING_DATA))
        }
    }

    override suspend fun getAlbumInfo(artistId: Int?, albumId: Int?) = flow<Resource<AlbumInfo>> {
        if (artistId == null || albumId == null)
            emit(Resource.Error(LyricsApplication.ERROR_FETCHING_DATA))
        else {
            val response = api.getAlbumInfo(artistId, albumId)

            if (response.success) emit(Resource.Success(response.result))
            else emit(Resource.Error(LyricsApplication.ERROR_FETCHING_DATA))
        }
    }

    override suspend fun getTrack(artistId: Int, albumId: Int, trackId: Int): Track {
        return api.getTrackInfo(artistId, albumId, trackId).result
    }

    override fun getHistoryItems() = historyDao.getAll()

    override suspend fun addToHistory(track: Track) {
        val newItem = fromTrackToHistoryItem(track)
        removeFromHistory(newItem.idTrack)
        historyDao.insert(newItem)
    }

    override suspend fun removeFromHistory(id: Int) = historyDao.delete(id)

    override suspend fun deleteHistoryItems() = historyDao.deleteAll()

    override fun getLibraryItems(query: String) = libraryDao.getAll(query)

    override fun isLibraryItem(id: Int) = libraryDao.exists(id)

    override suspend fun addToLibrary(track: Track) {
        val newItem = fromTrackToLibraryItem(track)
        libraryDao.insert(newItem)
    }

    override suspend fun deleteLibraryItem(id: Int) = libraryDao.delete(id)

    override suspend fun deleteLibraryItems() = libraryDao.deleteAll()
}