package shvyn22.flexinglyrics.repository.remote

import kotlinx.coroutines.flow.Flow
import shvyn22.flexinglyrics.data.remote.AlbumInfo
import shvyn22.flexinglyrics.data.remote.ArtistInfo
import shvyn22.flexinglyrics.data.remote.Track
import shvyn22.flexinglyrics.util.Resource

interface RemoteRepository {

    suspend fun searchTracks(query: String?): Flow<Resource<List<Track>>>

    suspend fun getArtistInfo(artistId: Int): Flow<Resource<ArtistInfo>>

    suspend fun getAlbumInfo(artistId: Int, albumId: Int): Flow<Resource<AlbumInfo>>

    suspend fun getTrack(artistId: Int, albumId: Int, trackId: Int): Flow<Resource<Track>>
}