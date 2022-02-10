package shvyn22.flexinglyrics.repository.remote

import io.reactivex.rxjava3.core.Observable
import shvyn22.flexinglyrics.data.remote.AlbumInfo
import shvyn22.flexinglyrics.data.remote.ArtistInfo
import shvyn22.flexinglyrics.data.remote.Track
import shvyn22.flexinglyrics.util.Resource

interface RemoteRepository {

    fun searchTracks(query: String?): Observable<Resource<List<Track>>>

    fun getArtistInfo(artistId: Int): Observable<Resource<ArtistInfo>>

    fun getAlbumInfo(artistId: Int, albumId: Int): Observable<Resource<AlbumInfo>>

    fun getTrack(artistId: Int, albumId: Int, trackId: Int): Observable<Resource<Track>>
}