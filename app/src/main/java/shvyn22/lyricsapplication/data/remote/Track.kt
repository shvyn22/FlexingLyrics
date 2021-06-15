package shvyn22.lyricsapplication.data.remote

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Track(
    @SerializedName("track")
    val track: String,

    @SerializedName("id_track")
    val idTrack: Int,

    @SerializedName("artist")
    val artist: String,

    @SerializedName("id_artist")
    val idArtist: Int,

    @SerializedName("album")
    val album: String,

    @SerializedName("id_album")
    val idAlbum: Int,

    @SerializedName("haslyrics")
    var hasLyrics: Boolean,

    @SerializedName("lyrics")
    val lyrics: String?
) : Parcelable
