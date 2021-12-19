package shvyn22.flexinglyrics.data.remote

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
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

    @Keep
    data class TrackInfo(

        @SerializedName("id_track")
        val idTrack: Int,

        @SerializedName("track")
        val track: String,

        @SerializedName("haslyrics")
        val hasLyrics: Boolean
    )
}
