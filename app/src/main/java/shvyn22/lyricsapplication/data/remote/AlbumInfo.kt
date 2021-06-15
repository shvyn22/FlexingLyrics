package shvyn22.lyricsapplication.data.remote

import com.google.gson.annotations.SerializedName

data class AlbumInfo(
    @SerializedName("id_album")
    val idAlbum: Int,

    @SerializedName("album")
    val album: String,

    @SerializedName("cover")
    val cover: String,

    @SerializedName("tracks")
    val tracks: List<TrackInfo>
) {
    data class TrackInfo(
        @SerializedName("id_track")
        val idTrack: Int,

        @SerializedName("track")
        val track: String,

        @SerializedName("haslyrics")
        val hasLyrics: Boolean
    )
}
