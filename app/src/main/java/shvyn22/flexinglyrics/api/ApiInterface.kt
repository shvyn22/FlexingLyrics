package shvyn22.flexinglyrics.api

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import shvyn22.flexinglyrics.data.remote.AlbumInfo
import shvyn22.flexinglyrics.data.remote.ArtistInfo
import shvyn22.flexinglyrics.data.remote.Track
import shvyn22.flexinglyrics.util.API_KEY
import shvyn22.flexinglyrics.util.API_LIMIT
import shvyn22.flexinglyrics.util.API_TYPE

interface ApiInterface {

    @GET("music")
    suspend fun searchTracks(
        @Query("q") query: String,
        @Query("limit") limit: Int = API_LIMIT,
        @Query("type") type: String = API_TYPE,
        @Query("apikey") apiKey: String = API_KEY
    ): ApiResponse<List<Track>>

    @GET("music/artists/{id_artist}")
    suspend fun getArtistInfo(
        @Path("id_artist") artistId: Int,
        @Query("apikey") apiKey: String = API_KEY
    ): ApiResponse<ArtistInfo>

    @GET("music/artists/{id_artist}/albums/{id_album}/tracks")
    suspend fun getAlbumInfo(
        @Path("id_artist") artistId: Int,
        @Path("id_album") albumId: Int,
        @Query("apikey") apiKey: String = API_KEY
    ): ApiResponse<AlbumInfo>

    @GET("music/artists/{id_artist}/albums/{id_album}/tracks/{id_track}/lyrics")
    suspend fun getTrackInfo(
        @Path("id_artist") artistId: Int,
        @Path("id_album") albumId: Int,
        @Path("id_track") trackId: Int,
        @Query("apikey") apiKey: String = API_KEY
    ): ApiResponse<Track>
}