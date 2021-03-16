package shvyn22.lyricsapplication.data.model

import com.google.gson.annotations.SerializedName

data class AlbumInfo(
    @SerializedName("id_album")
    val idAlbum: Int,

    val album: String,

    val cover: String,

    val tracks: List<TrackInfo>
) {
    data class TrackInfo(
        @SerializedName("id_track")
        val idTrack: Int,

        val track: String,

        @SerializedName("haslyrics")
        val hasLyrics: Boolean
    )
}
