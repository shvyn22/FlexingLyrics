package shvyn22.lyricsapplication.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Track(
    val track: String,

    @SerializedName("id_track")
    val idTrack: Int,

    val artist: String,

    @SerializedName("id_artist")
    val idArtist: Int,

    val album: String,

    @SerializedName("id_album")
    val idAlbum: Int,

    @SerializedName("haslyrics")
    var hasLyrics: Boolean,

    val lyrics: String?
) : Parcelable
