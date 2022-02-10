package shvyn22.flexinglyrics.api

import retrofit2.Call
import retrofit2.http.*
import shvyn22.flexinglyrics.BuildConfig
import shvyn22.flexinglyrics.data.remote.AlbumInfo
import shvyn22.flexinglyrics.data.remote.ArtistInfo
import shvyn22.flexinglyrics.data.remote.Track

interface ApiInterface {
    companion object {
        const val BASE_URL = "https://api.happi.dev/v1/"
        const val LIMIT = 50
        const val TYPE = "track"
        const val KEY = BuildConfig.API_KEY
    }

    @GET("music")
    fun searchTracks(
        @Query("q") query: String,
        @Query("limit") limit: Int = LIMIT,
        @Query("type") type: String = TYPE,
        @Query("apikey") apiKey: String = KEY
    ): Call<ApiResponse<List<Track>>>

    @GET("music/artists/{id_artist}")
    fun getArtistInfo(
        @Path("id_artist") artistId: Int,
        @Query("apikey") apiKey: String = KEY
    ): Call<ApiResponse<ArtistInfo>>

    @GET("music/artists/{id_artist}/albums/{id_album}/tracks")
    fun getAlbumInfo(
        @Path("id_artist") artistId: Int,
        @Path("id_album") albumId: Int,
        @Query("apikey") apiKey: String = KEY
    ): Call<ApiResponse<AlbumInfo>>

    @GET("music/artists/{id_artist}/albums/{id_album}/tracks/{id_track}/lyrics")
    fun getTrackInfo(
        @Path("id_artist") artistId: Int,
        @Path("id_album") albumId: Int,
        @Path("id_track") trackId: Int,
        @Query("apikey") apiKey: String = KEY
    ): Call<ApiResponse<Track>>
}