package shvyn22.lyricsapplication.api

import shvyn22.lyricsapplication.data.remote.AlbumInfo
import shvyn22.lyricsapplication.data.remote.ArtistInfo
import shvyn22.lyricsapplication.data.remote.Track
import shvyn22.lyricsapplication.util.*

class FakeApiImpl : ApiInterface {

    override suspend fun searchTracks(
        query: String,
        limit: Int,
        type: String,
        apiKey: String
    ): ApiResponse<List<Track>> {
        val result = tracks.filter { it.track.contains(query) }
        return ApiResponse(
            success = result.isNotEmpty(),
            result = result
        )
    }

    override suspend fun getArtistInfo(artistId: Int, apiKey: String): ApiResponse<ArtistInfo> {
        return if (artistId == 2640)
            ApiResponse(
                success = true,
                result = artistInfo1
            )
        else ApiResponse(
            success = false,
            result = artistInfoDefault
        )
    }

    override suspend fun getAlbumInfo(
        artistId: Int,
        albumId: Int,
        apiKey: String
    ): ApiResponse<AlbumInfo> {
        return if (artistId == 2640 && albumId == 503511)
            ApiResponse(
                success = true,
                result = albumInfo1
            )
        else ApiResponse(
            success = false,
            result = albumInfoDefault
        )
    }

    override suspend fun getTrackInfo(
        artistId: Int,
        albumId: Int,
        trackId: Int,
        apiKey: String
    ): ApiResponse<Track> {
        return if (artistId == 2640 && albumId == 503511 && trackId == 7515475)
            ApiResponse(
                success = true,
                result = track1.copy(
                    lyrics = TRACK_1_LYRICS
                )
            )
        else if (trackId == 5543867) ApiResponse(
            success = true,
            result = track3
        )
        else if (trackId == 7515479) ApiResponse(
            success = true,
            result = track4
        )
        else ApiResponse(
            success = false,
            result = track1
        )
    }
}