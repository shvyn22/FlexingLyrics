package shvyn22.flexinglyrics.data.remote

import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Keep
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
    val hasLyrics: Boolean,

    @SerializedName("lyrics")
    val lyrics: String?
) : Parcelable
