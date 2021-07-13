package shvyn22.lyricsapplication.repository

import kotlinx.coroutines.flow.Flow
import shvyn22.lyricsapplication.data.local.model.HistoryItem
import shvyn22.lyricsapplication.data.local.model.LibraryItem
import shvyn22.lyricsapplication.data.remote.AlbumInfo
import shvyn22.lyricsapplication.data.remote.ArtistInfo
import shvyn22.lyricsapplication.data.remote.Track
import shvyn22.lyricsapplication.util.Resource

interface Repository {
    suspend fun searchTracks(query: String?): Flow<Resource<List<Track>>>

    suspend fun getArtistInfo(artistId: Int?): Flow<Resource<ArtistInfo>>

    suspend fun getAlbumInfo(artistId: Int?, albumId: Int?): Flow<Resource<AlbumInfo>>

    suspend fun getTrack(artistId: Int, albumId: Int, trackId: Int): Track

    fun getHistoryItems(): Flow<List<HistoryItem>>

    suspend fun addToHistory(track: Track)

    suspend fun removeFromHistory(id: Int)

    suspend fun deleteHistoryItems()

    fun getLibraryItems(query: String): Flow<List<LibraryItem>>

    fun isLibraryItem(id: Int): Flow<Boolean>

    suspend fun addToLibrary(track: Track)

    suspend fun deleteLibraryItem(id: Int)

    suspend fun deleteLibraryItems()
}